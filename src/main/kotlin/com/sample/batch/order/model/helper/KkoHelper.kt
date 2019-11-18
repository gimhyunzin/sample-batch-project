package com.sample.batch.order.model.helper

class KkoHelper {

    // MSG_NOTICETALK_SENDER_KEY
    enum class Sender(val key: String) {
        PARTNER("")
    }

    // MSG_NOTICETALK_TMP_KEY
    enum class Template(val key: String) {
        ORDER_ALARM("ORDER00003"),
        CLAIM_ALARM("ORDER00004")
    }
}