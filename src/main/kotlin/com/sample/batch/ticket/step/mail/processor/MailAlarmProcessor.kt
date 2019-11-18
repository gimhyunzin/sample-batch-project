package com.sample.batch.ticket.step.mail.processor

import com.sample.batch.common.model.Mail
import com.sample.batch.common.property.BatchMailType
import com.sample.batch.ticket.common.ReflectColumn
import com.sample.batch.ticket.mapper.NamuSlaveTicketMapper
import com.sample.batch.ticket.model.Coupon
import com.sample.batch.ticket.model.Ticket
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.reflect.full.declaredMemberProperties


@Component
@StepScope
class MailAlarmProcessor : ItemProcessor<MutableList<Ticket>, MutableList<Mail>> {

    @Autowired
    lateinit var namuSlaveTicketMapper: NamuSlaveTicketMapper

    override fun process(ticketList: MutableList<Ticket>?): MutableList<Mail> {
        val mailList: MutableList<Mail> = mutableListOf()
        val ticketListIterator: Iterator<Ticket> = ticketList!!.iterator()

        for (ticket in ticketListIterator) {
            // 전체: coupon status -> 0, 1, 2
            // 완료: coupon status -> 1, 2
            // 미사용: coupon status -> 0
            val couponList: MutableList<Coupon> = namuSlaveTicketMapper.getAllCouponInformation(ticket.dealId)

            // 1. reflect ticket data class
            // 2. find declared member properties
            // 3. find special column (affEmail, csEmail, inventoryEmail) in for loop
            for (property in Ticket::class.declaredMemberProperties) {
                when(ReflectColumn().existCompanyContactManager(property, ticket)) {
                    true -> {
                        val mail = createMail(ticket, couponList)
                        // qa 팀 하드코딩 정보
                        // mail.email = "mtoeme@sample.com"
                        mail.email = ticket.affEmail
                        mail.name = ticket.affName
                        mailList.add(mail)
                    }
                }

                when(ReflectColumn().existCompanyCsManager(property, ticket)) {
                    true -> {
                        val mail = createMail(ticket, couponList)
                        // qa 팀 하드코딩 정보
                        // mail.email = "mtoeme@sample.com"
                        mail.email = ticket.csEmail
                        mail.name = ticket.csName
                        mailList.add(mail)
                    }
                }

                when(ReflectColumn().existCompanyStockManager(property, ticket)) {
                    true -> {
                        val mail = createMail(ticket, couponList)
                        // qa 팀 하드코딩 정보
                        // mail.email = "lys1318@sample.com"\
                        mail.email = ticket.inventoryEmail
                        mail.name = ticket.inventoryName
                        mailList.add(mail)
                    }
                }
            }
        }
        return mailList
    }

    private fun createMail(ticket: Ticket, couponList: MutableList<Coupon>): Mail {
        // 전체 티켓의 조건이 coupon status 0, 1, 2
        val allCouponSize: Int = couponList.size
        // 미사용 티켓의 조건은 coupon status 0
        val unusedCouponSize: Int = couponList.filter { it.couponStatus == 0 }.size
        // 완료 티켓의 조건은 coupon status 1, 2 지만 all - unused 로도 구해낼 수 있다.
        val completedCouponSize: Int = allCouponSize - unusedCouponSize

        return Mail(
                legacyId = ticket.legacyId,
                autoType = BatchMailType.TICKET.type,
                // ticket 유효기간
                tag1 = ticket.validEndTime,
                // 티켓 딜 이름
                tag2 = ticket.dealName,
                // 티켓 딜 번호
                tag3 = ticket.dealId.toString(),
                // 총 판매 티켓 수량
                tag4 = String.format("%,d", allCouponSize),
                // 사용 처리 완료 티켓 수량
                tag5 = String.format("%,d", completedCouponSize),
                // 미사용 처리 티켓 수량
                tag6 = String.format("%,d", unusedCouponSize)
        )
    }
}