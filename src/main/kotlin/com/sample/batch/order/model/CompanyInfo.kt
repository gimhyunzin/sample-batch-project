package com.sample.batch.order.model

data class CompanyInfo(
        var companyMId : Long = 0,
        var companyId : String = "",
        var companyName : String = "",

        var affName : String = "담당자",
        var affMobile : String = "",
        var affEmail : String = "",

        var csName : String = "담당자",
        var csMobile : String = "",
        var csEmail : String = "",

        var inventoryName : String = "담당자",
        var inventoryMobile : String = "",
        var inventoryEmail : String = ""
)