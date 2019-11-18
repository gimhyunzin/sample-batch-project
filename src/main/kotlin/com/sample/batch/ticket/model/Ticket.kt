package com.sample.batch.ticket.model

data class Ticket(
        var dealId: Long = 0,
        var validEndTime: String = "",
        var dealName: String = "",
        var companyName: String = "",
        // 제휴 담당자 휴대폰 번호
        var affMobile: String = "",
        // CS 담당자 휴대폰 번호
        var csMobile: String = "",
        // 제고 담당자 휴대폰 번호
        var inventoryMobile: String = "",
        // 제휴 담당자
        var companyContactManager: Boolean = false,
        // CS 담당자
        var companyCsManager: Boolean = false,
        // 제고 담당자
        var companyStockManager: Boolean = false,
        var affEmail: String = "",
        var csEmail: String = "",
        var inventoryEmail: String = "",
        var affName: String = "",
        var csName: String = "",
        var inventoryName: String = "",
        var legacyId: Long = 0
)