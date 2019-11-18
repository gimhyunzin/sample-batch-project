package com.sample.batch.order.model

data class OptionCoupon(
        var orderId: Long = 0,
        var optionId: Long = 0,

        var couponCode: String = "",
        var optionKind: String = "",
        var optionValue: String = "",
        var optionDate: String = ""
)