package com.sample.batch.order.mapper

import com.sample.batch.config.mapper.NamuMasterMapper
import org.springframework.stereotype.Repository

@Repository
@NamuMasterMapper
interface NamuMasterAdminAlarmMapper {
    fun updateClaimRequestLastKey(params: Map<String, Any>): Int
    fun updateClaimCompleteLastKey(params: Map<String, Any>): Int
    fun updateOrderCompleteLastKey(params: Map<String, Any>): Int
}