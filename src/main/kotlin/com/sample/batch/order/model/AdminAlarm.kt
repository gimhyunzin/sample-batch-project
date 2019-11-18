package com.sample.batch.order.model

import java.util.*

data class AdminAlarm(
        var claimId : Long = 0,
        var dealId : Long = 0,
        var orderId : Long = 0,
        var minusOrderId : Long = 0,

        var orderDate : Date? = null,
        var requestDate : Date? = null,
        var completeDate : Date? = null,
        var couponStatus: String = "",

        var dealInfo: DealInfo? = DealInfo(),
        var orderInfo: OrderInfo? = OrderInfo(),
        var optionCouponList: List<OptionCoupon> = mutableListOf()
)