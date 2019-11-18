package com.sample.batch.ticket.step.mail.reader

import com.sample.batch.common.property.ShareKey
import com.sample.batch.common.share.ShareData
import com.sample.batch.ticket.model.Ticket
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.annotation.BeforeStep
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
@StepScope
class MailAlarmReader : ItemReader<MutableList<Ticket>> {

    @Autowired
    lateinit var shareData: ShareData

    private var isRead: Boolean = false
    private var ticketList: MutableList<Ticket> = mutableListOf()


    @Throws(Exception::class)
    override fun read(): MutableList<Ticket>? {
        return when(!isRead) {
            true -> {
                isRead = true
                ticketList
            }
            false -> null
        }
    }

    @BeforeStep
    fun beforeShareData(stepExecution: StepExecution) {
        if(shareData.getData(ShareKey.TICKET_LIST) != null) {
            @Suppress("UNCHECKED_CAST")
            this.ticketList = shareData.getData(ShareKey.TICKET_LIST) as MutableList<Ticket>
        }
    }
}