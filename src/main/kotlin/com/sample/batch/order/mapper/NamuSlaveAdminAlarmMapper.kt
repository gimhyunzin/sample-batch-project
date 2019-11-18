package com.sample.batch.order.mapper

import com.sample.batch.order.model.AdminAlarm
import com.sample.batch.config.mapper.NamuSlaveMapper
import org.springframework.stereotype.Repository

@Repository
@NamuSlaveMapper
interface NamuSlaveAdminAlarmMapper {
    fun selectClaimRequestList(params: Map<String, Any>): MutableList<AdminAlarm>
    fun selectClaimCompleteList(params: Map<String, Any>): MutableList<AdminAlarm>
    fun selectOrderCompleteList(params: Map<String, Any>): MutableList<AdminAlarm>
}