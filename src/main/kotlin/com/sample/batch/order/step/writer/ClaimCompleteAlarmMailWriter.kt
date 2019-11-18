package com.sample.batch.order.step.writer

import com.sample.batch.common.model.Mail
import com.sample.batch.order.mapper.NetpathyMasterAdminAlarmMapper
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ClaimCompleteAlarmMailWriter : ItemWriter<MutableList<Mail>> {

    @Autowired
    lateinit var mapper: NetpathyMasterAdminAlarmMapper

    @Throws(Exception::class)
    override fun write(mailList: MutableList<out MutableList<Mail>>?) {
        val claimCompleteList = mailList!![0].toMutableList()

        when (claimCompleteList.isNotEmpty()) {
            true -> {
                mapper.createMail(claimCompleteList)
            }
        }
    }
}