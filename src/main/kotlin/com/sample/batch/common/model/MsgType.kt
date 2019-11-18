package com.sample.batch.common.model


enum class MsgType(val code: Int) {
    SMS(4),
    URL(5),
    MMS(6),
    BARCODE(7),
    KKO(8)
}