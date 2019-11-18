package com.sample.batch.ticket.mapper

import com.sample.batch.ticket.model.Coupon
import com.sample.batch.ticket.model.Ticket
import com.sample.batch.config.mapper.NamuSlaveMapper
import org.springframework.stereotype.Repository

@Repository
@NamuSlaveMapper
interface NamuSlaveTicketMapper {
    fun getTicketInformation(params: Map<String, Any>): MutableList<Ticket>
    fun getAllCouponInformation(dealId: Long): MutableList<Coupon>
}