package com.sample.batch.order.model

data class PartnerMember(
        var userMId : Long = 0,
        var userId : String = "",
        var userName : String = "",
        var userMobile : String = "",
        var userEmail : String = ""
)