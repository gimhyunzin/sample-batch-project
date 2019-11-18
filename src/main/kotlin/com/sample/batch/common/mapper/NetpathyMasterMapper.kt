package com.sample.batch.common.mapper

import com.sample.batch.common.model.Mail
import com.sample.batch.config.mapper.NetpathyMasterMapper
import org.springframework.stereotype.Repository

@Repository
@NetpathyMasterMapper
interface NetpathyMasterMapper {
    fun createMail(mailList: MutableList<Mail>): Int
}