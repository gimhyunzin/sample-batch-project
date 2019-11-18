package com.sample.batch.order.model.template

data class AdminAlarmMsgTemplate(
        var messageSubject: String = "",
        var messageBody: String = "",

        var orderNameMaxLength: Int = 5,
        var dealNameMaxLength: Int = 16,
        var dealIdMaxLength: Int = 7,
        var optionNameMaxLength: Int = 22,
        var optionIdMaxLength: Int = 9
)