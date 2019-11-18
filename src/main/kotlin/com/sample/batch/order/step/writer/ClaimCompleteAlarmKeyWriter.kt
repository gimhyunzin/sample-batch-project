package com.sample.batch.order.step.writer

import com.sample.batch.order.mapper.NamuMasterAdminAlarmMapper
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ClaimCompleteAlarmKeyWriter : ItemWriter<Long> {

    private val logger = LoggerFactory.getLogger(ClaimCompleteAlarmKeyWriter::class.java)

    @Value("\${spring.profiles}")
    lateinit var profiles: String

    @Autowired
    lateinit var mapper: NamuMasterAdminAlarmMapper

    override fun write(items: MutableList<out Long>?) {
        when (items?.isNotEmpty()) {
            true -> {
                val lastClaimId = items[0]
                when (0 < lastClaimId) {
                    true -> {

                        val params = mutableMapOf<String, Any>()
                        params["profiles"] = profiles.toUpperCase()
                        params["lastKey"] = lastClaimId

                        mapper.updateClaimCompleteLastKey(params)

                        logger.info("======== [CLAIM_COMPLETE_ALARM] =====>>> [${profiles.toUpperCase()}] LAST KEY : === [$lastClaimId] ====")
                    }
                }
            }
        }
    }
}