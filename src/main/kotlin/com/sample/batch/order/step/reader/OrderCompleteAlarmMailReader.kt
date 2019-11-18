package com.sample.batch.order.step.reader

import com.sample.batch.common.property.ShareKey.ORDER_COMPLETE_DATA_LIST
import com.sample.batch.common.property.ShareKey.ORDER_COMPLETE_LAST_KEY
import com.sample.batch.common.share.ShareData
import com.sample.batch.order.model.AdminAlarm
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.annotation.AfterStep
import org.springframework.batch.core.annotation.BeforeStep
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@StepScope
class OrderCompleteAlarmMailReader : ItemReader<MutableList<AdminAlarm>> {

    @Autowired
    lateinit var shareData: ShareData

    private var isRead: Boolean = false
    private var dataList: MutableList<AdminAlarm> = mutableListOf()

    override fun read(): MutableList<AdminAlarm>? {

        return when (isRead) {
            true -> null
            false -> {
                isRead = true

                dataList
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    @BeforeStep
    fun loadShareStepExecution(stepExecution: StepExecution) {
        this.dataList = shareData.getData(ORDER_COMPLETE_DATA_LIST) as MutableList<AdminAlarm>? ?: mutableListOf()
    }

    @AfterStep
    fun saveShareStepExecution(stepExecution: StepExecution) {
        val lastOrderId = if (dataList.isEmpty()) 0 else dataList[0].orderId
        shareData.putData(ORDER_COMPLETE_LAST_KEY, lastOrderId)
    }
}