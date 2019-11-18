package com.sample.batch.order.step.processor

import com.google.gson.Gson
import com.sample.batch.common.model.Mail
import com.sample.batch.common.property.BatchMailType
import com.sample.batch.order.model.*
import com.sample.batch.order.model.helper.AlarmHelper
import com.sample.batch.order.model.template.AdminAlarmTargetTemplate
import org.slf4j.LoggerFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component

@Component
@StepScope
class ClaimCompleteAlarmMailProcessor : ItemProcessor<MutableList<AdminAlarm>, MutableList<Mail>> {

    private val logger = LoggerFactory.getLogger(ClaimCompleteAlarmMailProcessor::class.java)

    override fun process(dataList: MutableList<AdminAlarm>?): MutableList<Mail> {

        val mailList: MutableList<Mail> = mutableListOf()
        val iterator: Iterator<AdminAlarm> = dataList!!.iterator()

        for (data in iterator) {
            mailList.addAll(this.createMailList(data))
        }

        return mailList
    }

    private fun createMailList(data : AdminAlarm): MutableList<Mail> {

        val mailList: MutableList<Mail> = mutableListOf()

        val dealInfo = data.dealInfo ?: DealInfo()
        val dealDetail = dealInfo.dealDetail ?: DealDetail()

        val mdInfo = dealInfo.mdInfo ?: PartnerMember()
        val amdInfo = dealInfo.amdInfo ?: PartnerMember()
        val companyInfo = dealInfo.companyInfo ?: CompanyInfo()

        val mdMId = mdInfo.userMId
        val amdMId = amdInfo.userMId
        val companyMId = companyInfo.companyMId

        val mdName = mdInfo.userName
        val amdName = amdInfo.userName
        val csName = companyInfo.csName
        val affName = companyInfo.affName
        val inventoryName = companyInfo.inventoryName

        val mdEmail = mdInfo.userEmail
        val amdEmail = amdInfo.userEmail
        val csEmail = companyInfo.csEmail
        val affEmail = companyInfo.affEmail
        val inventoryEmail = companyInfo.inventoryEmail

        val alarmTarget = dealDetail.orderCancelAlarmTarget.takeIf { it.isNotEmpty() } ?: "{}"
        val targetInfo = Gson().fromJson(alarmTarget, AdminAlarmTargetTemplate::class.java)

        val md = targetInfo.md
        val amd = targetInfo.amd
        val cs = targetInfo.cs
        val aff = targetInfo.aff
        val inventory = targetInfo.inventory

        when (md == 1 && mdEmail.isNotEmpty()) {
            true -> {
                val mailInfo = this.createMailInfo(data)
                mailInfo.legacyId = mdMId
                mailInfo.name = mdName
                mailInfo.email = mdEmail
                mailList.add(mailInfo)
            }
        }
        when (amd == 1 && amdEmail.isNotEmpty()) {
            true -> {
                val mailInfo = this.createMailInfo(data)
                mailInfo.legacyId = amdMId
                mailInfo.name = amdName
                mailInfo.email = amdEmail
                mailList.add(mailInfo)
            }
        }
        when (cs == 1 && csEmail.isNotEmpty()) {
            true -> {
                val mailInfo = this.createMailInfo(data)
                mailInfo.legacyId = companyMId
                mailInfo.name = csName
                mailInfo.email = csEmail
                mailList.add(mailInfo)
            }
        }
        when (aff == 1 && affEmail.isNotEmpty()) {
            true -> {
                val mailInfo = this.createMailInfo(data)
                mailInfo.legacyId = companyMId
                mailInfo.name = affName
                mailInfo.email = affEmail
                mailList.add(mailInfo)
            }
        }
        when (inventory == 1 && inventoryEmail.isNotEmpty()) {
            true -> {
                val mailInfo = this.createMailInfo(data)
                mailInfo.legacyId = companyMId
                mailInfo.name = inventoryName
                mailInfo.email = inventoryEmail
                mailList.add(mailInfo)
            }
        }

        logger.info("====== [CLAIM_COMPLETE_ALARM] =================== [DEAL_ID] ${dealInfo.dealId} =================== ")
        logger.info("====== [CLAIM_COMPLETE_ALARM] ====== MD  ($md) ====== : ${AlarmHelper.maskName(mdName)} / ${AlarmHelper.maskEmail(mdEmail)}")
        logger.info("====== [CLAIM_COMPLETE_ALARM] ====== AMD ($amd) ====== : ${AlarmHelper.maskName(amdName)} / ${AlarmHelper.maskEmail(amdEmail)}")
        logger.info("====== [CLAIM_COMPLETE_ALARM] ====== CS  ($cs) ====== : ${AlarmHelper.maskName(csName)} / ${AlarmHelper.maskEmail(csEmail)}")
        logger.info("====== [CLAIM_COMPLETE_ALARM] ====== AFF ($aff) ====== : ${AlarmHelper.maskName(affName)} / ${AlarmHelper.maskEmail(affEmail)}")
        logger.info("====== [CLAIM_COMPLETE_ALARM] ====== INV ($inventory) ====== : ${AlarmHelper.maskName(inventoryName)} / ${AlarmHelper.maskEmail(inventoryEmail)}")

        return mailList
    }

    private fun createMailInfo(data: AdminAlarm): Mail {

        val orderInfo = data.orderInfo ?: OrderInfo()
        val optionCouponList = data.optionCouponList

        return Mail(
                autoType = BatchMailType.CLAIM_ALARM.type,

                // 취소일시
                tag1 = AlarmHelper.convertMailDate(data.completeDate),
                // 딜 명
                tag2 = AlarmHelper.convertText(orderInfo.dealName, 17),
                // 딜 번호
                tag3 = data.dealId.toString(),

                // 고객명
                tag4 = AlarmHelper.maskName(orderInfo.orderName),
                // 연락처
                tag5 = AlarmHelper.maskMobile(orderInfo.orderMobile),

                // 옵션명
                htmlTag1 = AlarmHelper.getFullOptionString(optionCouponList)
        )
    }
}