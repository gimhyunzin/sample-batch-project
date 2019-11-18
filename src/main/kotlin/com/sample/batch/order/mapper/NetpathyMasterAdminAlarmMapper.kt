package com.sample.batch.order.mapper

import com.sample.batch.common.model.Mail
import com.sample.batch.config.mapper.NetpathyMasterMapper
import org.springframework.stereotype.Repository

@Repository
@NetpathyMasterMapper
interface NetpathyMasterAdminAlarmMapper {
    fun createMail(mailList: MutableList<Mail>): Int
}