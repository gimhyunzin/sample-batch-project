package com.sample.batch.ticket.step.mail.writer

import com.sample.batch.common.mapper.NetpathyMasterMapper
import com.sample.batch.common.model.Mail
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MailAlarmWriter : ItemWriter<MutableList<Mail>> {

    @Autowired
    lateinit var netpathyMasterMapper: NetpathyMasterMapper

    @Throws(Exception::class)
    override fun write(mailMutableList: MutableList<out MutableList<Mail>>?) {
        val mailList: MutableList<Mail> = mailMutableList!![0].toMutableList()

        when(mailList.isNotEmpty()) {
            true -> netpathyMasterMapper.createMail(mailList)
        }
    }
}