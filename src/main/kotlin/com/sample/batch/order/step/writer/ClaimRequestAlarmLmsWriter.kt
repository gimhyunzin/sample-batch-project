package com.sample.batch.order.step.writer

import com.sample.batch.common.model.Msg
import com.sample.batch.order.mapper.NproMasterAdminAlarmMapper
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ClaimRequestAlarmLmsWriter : ItemWriter<MutableList<Msg>> {

    @Autowired
    lateinit var mapper: NproMasterAdminAlarmMapper

    override fun write(messageList: MutableList<out MutableList<Msg>>?) {

        val lmsList = messageList!![0].toMutableList()

        when (lmsList.isNotEmpty()) {
            true -> {
                mapper.createMsgLms(lmsList)
            }
        }
    }
}