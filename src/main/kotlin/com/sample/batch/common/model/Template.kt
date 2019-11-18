package com.sample.batch.common.model

data class Template(
        var messageBody: String = "",
        var messageFooter: String = "",
        var dealIdMaxLength: Int = 0,
        var dealNameMaxLength: Int = 0,
        var companyNameMaxLength: Int = 0
)