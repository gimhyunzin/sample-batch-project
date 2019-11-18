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
class ClaimRequestAlarmKeyReader : ItemReader<Long> {

    @Autowired
    lateinit var shareData: ShareData

    private var isRead: Boolean = false
    private var lastClaimId: Long = 0

    override fun read(): Long? {

        return when (isRead) {
            true -> null
            false -> {
                isRead = true

                lastClaimId
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    @BeforeStep
    fun loadShareStepExecution(stepExecution: StepExecution) {
        this.lastClaimId = shareData.getData(CLAIM_REQUEST_LAST_KEY) as Long? ?: 0
    }
}