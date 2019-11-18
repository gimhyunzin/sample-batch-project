package com.sample.batch.order.step.reader

import com.sample.batch.common.share.ShareData
import com.sample.batch.common.property.ShareKey.*
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.annotation.BeforeStep
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@StepScope
class OrderCompleteAlarmKeyReader : ItemReader<Long> {

    @Autowired
    lateinit var shareData: ShareData

    private var isRead: Boolean = false
    private var lastOrderId: Long = 0

    override fun read(): Long? {

        return when (isRead) {
            true -> null
            false -> {
                isRead = true

                lastOrderId
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    @BeforeStep
    fun loadShareStepExecution(stepExecution: StepExecution) {
        this.lastOrderId = shareData.getData(ORDER_COMPLETE_LAST_KEY) as Long? ?: 0
    }
}