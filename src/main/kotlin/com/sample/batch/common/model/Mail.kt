package com.sample.batch.common.model

data class Mail (
        var legacyId: Long = 0,
        var autoType: String = "",
        var email: String = "",
        var name: String = "",
        var sendYn: String = "N",
        var tag1: String = "",
        var tag2: String = "",
        var tag3: String = "",
        var tag4: String = "",
        var tag5: String = "",
        var tag6: String = "",
        var htmlTag1: String = ""
)