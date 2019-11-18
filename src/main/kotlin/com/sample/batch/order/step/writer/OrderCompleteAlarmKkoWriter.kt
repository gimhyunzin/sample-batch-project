package com.sample.batch.order.step.writer

import com.sample.batch.common.model.Msg
import com.sample.batch.common.property.ShareKey.ORDER_COMPLETE_KKO_LIST
import com.sample.batch.common.share.ShareData
import com.sample.batch.order.mapper.NproMasterAdminAlarmMapper
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.annotation.AfterStep
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class OrderCompleteAlarmKkoWriter : ItemWriter<MutableList<Msg>> {

    @Autowired
    lateinit var shareData: ShareData

    @Autowired
    lateinit var mapper: NproMasterAdminAlarmMapper

    private var dataList: MutableList<Msg> = mutableListOf()

    override fun write(messageList: MutableList<out MutableList<Msg>>?) {

        val kkoList = messageList!![0].toMutableList()

        when (kkoList.isNotEmpty()) {
            true -> {
                mapper.createMsgKko(kkoList)

                dataList = kkoList
            }
        }
    }

    @AfterStep
    fun saveShareStepExecution(stepExecution: StepExecution) {
        shareData.putData(ORDER_COMPLETE_KKO_LIST, dataList)
    }
}