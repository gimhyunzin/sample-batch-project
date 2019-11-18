package com.sample.batch.ticket.step.lms.writer

import com.sample.batch.common.model.Msg
import com.sample.batch.ticket.mapper.NproMasterTicketMapper
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
@StepScope
class LMSAlarmWriter : ItemWriter<MutableList<Msg>> {

    @Autowired
    lateinit var nproMasterTicketMapper: NproMasterTicketMapper

    @Throws(Exception::class)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=[Exception::class])
    override fun write(msgMutableList: MutableList<out MutableList<Msg>>?) {
        val msgList: MutableList<Msg> = msgMutableList!![0].toMutableList()
        val lmsList: List<Msg> = msgList.distinctBy { it.dealId }

        if(lmsList.isNotEmpty()) {
            nproMasterTicketMapper.createLmsMessage(lmsList)
            mergeMessage(msgList, lmsList)
            nproMasterTicketMapper.createSmsMessage(msgList)
        }
    }

    private fun mergeMessage(msgList: MutableList<Msg>, lmsList: List<Msg>) {
        for(sms in msgList) {
            for(lms in lmsList) {
                when(sms.dealId == lms.dealId) {
                    true -> sms.contSeq = lms.contSeq
                }
            }
        }
    }
}