package com.sample.batch.order.step.reader

import com.sample.batch.common.property.ShareKey.ORDER_COMPLETE_DATA_LIST
import com.sample.batch.common.share.ShareData
import com.sample.batch.order.mapper.NamuSlaveAdminAlarmMapper
import com.sample.batch.order.model.AdminAlarm
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.annotation.AfterStep
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
@StepScope
class OrderCompleteAlarmKkoReader : ItemReader<MutableList<AdminAlarm>> {

    private val logger = LoggerFactory.getLogger(OrderCompleteAlarmKkoReader::class.java)

    @Value("\${spring.profiles}")
    lateinit var profiles: String

    @Value("#{'\${property.alarm-test-partners-on}'.split(',')}")
    lateinit var onPartners: List<String>

    @Value("#{'\${property.alarm-test-partners-off}'.split(',')}")
    lateinit var offPartners: List<String>

    @Autowired
    lateinit var shareData: ShareData

    @Autowired
    lateinit var mapper: NamuSlaveAdminAlarmMapper

    private var isRead: Boolean = false
    private var dataList: MutableList<AdminAlarm> = mutableListOf()

    override fun read(): MutableList<AdminAlarm>? {

        return when (isRead) {
            true -> null
            false -> {
                isRead = true

                val params = mutableMapOf<String, Any>()
                params["profiles"] = profiles.toUpperCase()
                params["onPartners"] = onPartners.filter { it.trim().isNotEmpty() }
                params["offPartners"] = offPartners.filter { it.trim().isNotEmpty() }

                val orderCompleteList = mapper.selectOrderCompleteList(params)
                dataList = orderCompleteList

                orderCompleteList
            }
        }
    }

    @AfterStep
    fun saveShareStepExecution(stepExecution: StepExecution) {
        shareData.putData(ORDER_COMPLETE_DATA_LIST, dataList)

        logger.info("========= [ALARM_TEST_PARTNERS_ON] ${profiles.toUpperCase()} : ${onPartners.takeIf { it.isNotEmpty() }} =========")
        logger.info("========= [ALARM_TEST_PARTNERS_OFF] ${profiles.toUpperCase()} : ${offPartners.takeIf { it.isNotEmpty() }} =========")
        logger.info("========= [ORDER_COMPLETE_DATA_LIST] >>>> size : [${dataList.size}] =========")
    }
}