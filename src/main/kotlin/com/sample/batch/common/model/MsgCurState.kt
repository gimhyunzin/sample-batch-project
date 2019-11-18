package com.sample.batch.common.model


enum class MsgCurState(val code: Int) {
    REQUEST(0),
    SENDING(1),
    COMPLETE(2),
    RECEIVED(3)
}