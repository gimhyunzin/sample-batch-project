package com.sample.batch.order.mapper

import com.sample.batch.common.model.Msg
import com.sample.batch.config.mapper.NproMasterMapper
import org.springframework.stereotype.Repository

@Repository
@NproMasterMapper
interface NproMasterAdminAlarmMapper {
    fun createMsgKko(messageList: MutableList<Msg>): Int
    fun createMsgLms(messageList: MutableList<Msg>): Int
}