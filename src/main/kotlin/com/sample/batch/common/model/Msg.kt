package com.sample.batch.common.model

import com.sample.batch.order.model.Receiver
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Msg(
        var msgSeq: Int = 0,
        var curState: Int = MsgCurState.REQUEST.code,
        var reqDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
        var callTo: String = "",
        var callFrom: String = "15884763",
        var smsTxt: String = "",
        var msgType: Int = MsgType.MMS.code,
        var msgTypeResend: Int = MsgType.MMS.code,
        var contSeq: Int = 0,
        var kkoSenderKey: String = "",
        var kkoTemplateKey: String = "",
        var kkoBody: String = "",
        var kkoSubject: String = "",
        var mmsBody:String = "",
        var mmsSubject: String = "",

        // ------------------- custom area -----------------------//
        var receiverList: MutableList<Receiver> = mutableListOf(),
        var dealId: Long = 0
)