package com.sample.batch.order.step.processor

import com.sample.batch.common.model.Msg
import com.sample.batch.common.model.MsgCurState
import com.sample.batch.common.model.MsgType
import com.sample.batch.order.model.helper.KkoHelper
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component

@Component
@StepScope
class ClaimRequestAlarmLmsProcessor : ItemProcessor<MutableList<Msg>, MutableList<Msg>> {

    override fun process(dataList: MutableList<Msg>?): MutableList<Msg> {

        val lmsMessageList = mutableListOf<Msg>()
        val iterator: Iterator<Msg> = dataList!!.iterator()

        loop@
        for (data in iterator) {
            when (data.contSeq) {
                0 -> continue@loop
                else -> lmsMessageList.addAll(this.createLmsMessageList(data))
            }
        }

        return lmsMessageList
    }

    private fun createLmsMessageList(data : Msg): MutableList<Msg> {

        val lmsMessageList: MutableList<Msg> = mutableListOf()

        for (receiver in data.receiverList) {

            val lmsMessage = Msg()

            lmsMessage.smsTxt = "취소 신청 알림"
            lmsMessage.msgType = MsgType.KKO.code
            lmsMessage.msgTypeResend = MsgType.MMS.code
            lmsMessage.curState = MsgCurState.SENDING.code
            lmsMessage.kkoSenderKey = KkoHelper.Sender.PARTNER.key
            lmsMessage.kkoTemplateKey = KkoHelper.Template.CLAIM_ALARM.key

            lmsMessage.contSeq = data.contSeq
            lmsMessage.callTo = receiver.mobile
            lmsMessage.reqDate = receiver.reqDate

            lmsMessageList.add(lmsMessage)
        }

        return lmsMessageList
    }
}