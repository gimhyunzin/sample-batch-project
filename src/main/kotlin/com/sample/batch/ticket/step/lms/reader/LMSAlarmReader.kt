package com.sample.batch.ticket.step.lms.reader

import com.sample.batch.ticket.mapper.NamuSlaveTicketMapper
import com.sample.batch.common.property.ShareKey
import com.sample.batch.common.share.ShareData
import com.sample.batch.ticket.model.Coupon
import com.sample.batch.ticket.model.Ticket
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
@StepScope
class LMSAlarmReader : ItemReader<MutableList<Ticket>> {

    private var isRead: Boolean = false

    @Autowired
    lateinit var shareData: ShareData

    @Autowired
    lateinit var namuSlaveMapper: NamuSlaveTicketMapper

    @Value("#{'\${property.alarm-test-partners-on}'.split(',')}")
    lateinit var onPartners: List<String>

    @Value("#{'\${property.alarm-test-partners-off}'.split(',')}")
    lateinit var offPartners: List<String>

    @Throws(Exception::class)
    override fun read(): MutableList<Ticket>? {
        val resultTicketList: MutableList<Ticket> = mutableListOf()

        return when(!isRead) {
            true -> {

                val params = mutableMapOf<String, Any>()
                params["onPartners"] = onPartners.filter { it.trim().isNotEmpty() }
                params["offPartners"] = offPartners.filter { it.trim().isNotEmpty() }

                val ticketList: MutableList<Ticket> = namuSlaveMapper.getTicketInformation(params)
                isRead = true

                for(ticket in ticketList.iterator()) {
                    // 전체: coupon status -> 0, 1, 2
                    // 완료: coupon status -> 1, 2
                    // 미사용: coupon status -> 0
                    val couponList: MutableList<Coupon> = namuSlaveMapper.getAllCouponInformation(ticket.dealId)
                    if(existUnusedTicket(couponList)) {
                        resultTicketList.add(ticket)
                    }
                }
                shareData.putData(ShareKey.TICKET_LIST, resultTicketList)
                resultTicketList
            }
            false -> null
        }
    }

    private fun existUnusedTicket(couponList: MutableList<Coupon>): Boolean {
        // 당일 만료되는 딜 안에 미사용 티켓이 하나도 없으면 안내 메일을 보내지 않는다.
        // 미사용 티켓의 조건은 coupon status 0
        val unusedCouponSize: Int = couponList.filter { it.couponStatus == 0 }.size

        return when(unusedCouponSize > 0) {
            true -> true
            false -> false
        }
    }
}