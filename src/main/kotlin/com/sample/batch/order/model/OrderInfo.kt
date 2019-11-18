package com.sample.batch.order.model

data class OrderInfo(
        var orderId: Long = 0,
        var dealId: Long = 0,
        var dealName: String = "",
        var senderName: String = "",
        var orderName: String = "",
        var orderMobile: String = "",
        var orderEmail: String = ""
)