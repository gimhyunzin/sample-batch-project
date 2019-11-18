package com.sample.batch.ticket.step.lms.processor

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.sample.batch.common.model.Msg
import com.sample.batch.common.model.Template
import com.sample.batch.common.property.BatchTemplate
import com.sample.batch.ticket.common.ReflectColumn
import com.sample.batch.ticket.model.Ticket
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileReader
import java.io.InputStream
import kotlin.reflect.full.declaredMemberProperties

@Component
@StepScope
class LMSAlarmProcessor : ItemProcessor<MutableList<Ticket>, MutableList<Msg>> {

    override fun process(ticketList: MutableList<Ticket>?): MutableList<Msg> {
        val messageList: MutableList<Msg> = mutableListOf()
        // create template message
        val uTicketTemplate: Template = this.createUTicketTemplate()

        for (ticket in ticketList!!.iterator()) {
            // create template message
            val copyUTicketTemplate: Template = uTicketTemplate.copy()
            val message: String = this.createMessage(copyUTicketTemplate, ticket)

            createSmsMessage(messageList, ticket, message)
        }
        return messageList
    }

    private fun createUTicketTemplate(): Template {
        val template: Template
        val gson = Gson()

        val classPathResource = ClassPathResource(BatchTemplate.TICKET_FILEPATH.value)
        val inputStream: InputStream = classPathResource.inputStream
        val tempFile: File = File.createTempFile("temp", ".json")

        try {
            FileUtils.copyInputStreamToFile(inputStream, tempFile)
            template = gson.fromJson(JsonReader(FileReader(tempFile)), Template::class.java)

        } finally {
            IOUtils.closeQuietly(inputStream)
        }
        return template
    }

    private fun createMessage(template: Template, ticket: Ticket): String {
        var messageFooter: String = template.messageFooter
        messageFooter = messageFooter.replace(oldValue = "{0}", newValue = this.replaceSMSTxt(ticket.dealId.toString(), template.dealIdMaxLength))
        messageFooter = messageFooter.replace(oldValue = "{1}", newValue = this.replaceSMSTxt(ticket.dealName, template.dealNameMaxLength))
        messageFooter = messageFooter.replace(oldValue = "{2}", newValue = this.replaceSMSTxt(ticket.companyName, template.companyNameMaxLength))

        template.messageFooter = messageFooter

        return template.messageBody.plus(template.messageFooter)
    }

    // check dealId, dealName, companyName Length (MSG_DATA SMS_TXT column length check)
    private fun replaceSMSTxt(data: String, targetMaxLength: Int): String {
        return when (data.length > targetMaxLength) {
            true -> data.substring(0, targetMaxLength + 1).plus("...")
            false -> data
        }
    }

    private fun createSmsMessage(lmsList: MutableList<Msg>, ticket: Ticket, message: String) {
        val smsList: MutableList<Msg> = lmsList

        val title = "미사용 티켓 처리 알림"
        val smsTxt = "미사용 티켓 알림 LMS"

        // 1. reflect ticket data class
        // 2. find declared member properties
        // 3. find special column (companyContactManager, companyCsManager, companyStockManager) in for loop
        for (property in Ticket::class.declaredMemberProperties) {
            when(ReflectColumn().existCompanyContactManager(property, ticket)) {
                true -> {
                    // qa 팀 하드코딩 정보
                    //smsList.add(Msg(smsTxt = smsTxt, msgType = MsgType.MMS.code, callTo = "01022990534"))
                    smsList.add(Msg(smsTxt = smsTxt, mmsBody = message, mmsSubject = title, callTo = ticket.affMobile.replace(oldValue = "-", newValue = ""), dealId = ticket.dealId))
                }
            }

            when(ReflectColumn().existCompanyCsManager(property, ticket)) {
                true -> {
                    // qa 팀 하드코딩 정보
                    // smsList.add(Msg(smsTxt = smsTxt, msgType = MsgType.MMS.code, callTo = "01022990534"))
                    smsList.add(Msg(smsTxt = smsTxt, mmsBody = message, mmsSubject = title, callTo = ticket.csMobile.replace(oldValue = "-", newValue = ""), dealId = ticket.dealId))
                }
            }

            when(ReflectColumn().existCompanyStockManager(property, ticket)) {
                true -> {
                    // qa 팀 하드코딩 정보
                    // smsList.add(Msg(smsTxt = smsTxt, msgType = MsgType.MMS.code, callTo = "01043642021"))
                    smsList.add(Msg(smsTxt = smsTxt, mmsBody = message, mmsSubject = title, callTo = ticket.inventoryMobile.replace(oldValue = "-", newValue = ""), dealId = ticket.dealId))
                }
            }
        }
    }
}