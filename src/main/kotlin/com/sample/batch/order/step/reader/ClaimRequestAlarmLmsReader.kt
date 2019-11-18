package com.sample.batch.order.step.reader

import com.sample.batch.common.model.Msg
import com.sample.batch.common.property.ShareKey.CLAIM_REQUEST_KKO_LIST
import com.sample.batch.common.share.ShareData
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.annotation.BeforeStep
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@StepScope
class ClaimRequestAlarmLmsReader : ItemReader<MutableList<Msg>> {

    @Autowired
    lateinit var shareData: ShareData

    private var isRead: Boolean = false
    private var dataList: MutableList<Msg> = mutableListOf()

    override fun read(): MutableList<Msg>? {

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
        this.dataList = shareData.getData(CLAIM_REQUEST_KKO_LIST) as MutableList<Msg>? ?: mutableListOf()
    }
}