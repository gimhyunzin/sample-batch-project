package com.sample.batch.ticket.mapper

import com.sample.batch.common.model.Msg
import com.sample.batch.config.mapper.NproMasterMapper
import org.springframework.stereotype.Repository

@Repository
@NproMasterMapper
interface NproMasterTicketMapper {
    fun createLmsMessage(msgList: List<Msg>): Int
    fun createSmsMessage(msgList: MutableList<Msg>): Int
}