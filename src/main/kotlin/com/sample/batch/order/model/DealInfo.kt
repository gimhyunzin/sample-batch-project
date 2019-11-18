package com.sample.batch.order.model

data class DealInfo(
        var dealId: Long = 0,

        var mdId : String? = "",
        var managerId : String? = "",
        var companyId : String? = "",

        var mdInfo : PartnerMember? = PartnerMember(),
        var amdInfo : PartnerMember? = PartnerMember(),
        var companyInfo: CompanyInfo? = CompanyInfo(),

        var dealDetail: DealDetail? = DealDetail()
)