package com.sample.batch.order.model

data class DealDetail(
        var dealId: Long = 0,
        var orderFinishAlarmTarget: String = "{}",
        var orderCancelAlarmTarget: String = "{}",
        var orderMannerAlarmFlag: Int = 0
)