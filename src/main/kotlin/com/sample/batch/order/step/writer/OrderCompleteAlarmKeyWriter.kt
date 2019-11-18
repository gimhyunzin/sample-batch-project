package com.sample.batch.order.step.writer

import com.sample.batch.order.mapper.NamuMasterAdminAlarmMapper
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class OrderCompleteAlarmKeyWriter : ItemWriter<Long> {

    private val logger = LoggerFactory.getLogger(OrderCompleteAlarmKeyWriter::class.java)

    @Value("\${spring.profiles}")
    lateinit var profiles: String

    @Autowired
    lateinit var mapper: NamuMasterAdminAlarmMapper

    override fun write(items: MutableList<out Long>?) {
        when (items?.isNotEmpty()) {
            true -> {
                val lastOrderId = items[0]
                when (0 < lastOrderId) {
                    true -> {

                        val params = mutableMapOf<String, Any>()
                        params["profiles"] = profiles.toUpperCase()
                        params["lastKey"] = lastOrderId

                        mapper.updateOrderCompleteLastKey(params)

                        logger.info("======== [ORDER_COMPLETE_ALARM] =====>>> [${profiles.toUpperCase()}] LAST KEY : === [$lastOrderId] ====")
                    }
                }
            }
        }
    }
}