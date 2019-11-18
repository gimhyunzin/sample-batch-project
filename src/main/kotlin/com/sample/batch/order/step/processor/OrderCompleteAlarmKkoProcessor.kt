package com.sample.batch.order.step.processor

import com.google.gson.Gson
import com.sample.batch.common.model.Msg
import com.sample.batch.order.model.*
import com.sample.batch.order.model.helper.AlarmHelper
import com.sample.batch.order.model.template.AdminAlarmMsgTemplate
import com.sample.batch.order.model.template.AdminAlarmTargetTemplate
import org.slf4j.LoggerFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
@StepScope
class OrderCompleteAlarmKkoProcessor : ItemProcessor<MutableList<AdminAlarm>, MutableList<Msg>> {

    private val logger = LoggerFactory.getLogger(OrderCompleteAlarmKkoProcessor::class.java)

    private var adminAlarmMsgTemplate = AlarmHelper.createMsgTemplate()

    override fun process(dataList: MutableList<AdminAlarm>?): MutableList<Msg> {

        val messageList = mutableListOf<Msg>()
        val iterator: Iterator<AdminAlarm> = dataList!!.iterator()

        for (data in iterator) {

            val kkoMessage = this.createKkoMessage(data)
            val receiverList = kkoMessage.receiverList

            when (receiverList.isNotEmpty()) {
                true -> messageList.add(kkoMessage)
            }
        }

        return messageList
    }

    private fun createKkoMessage(data : AdminAlarm): Msg {

        val template = this.convertMsgTemplate(data)

        val msgBody = template.messageBody
        val msgSubject = template.messageSubject

        val kkoMessage = Msg()
        kkoMessage.mmsBody = msgBody
        kkoMessage.mmsSubject = msgSubject
        kkoMessage.kkoBody = "$msgSubject\n$msgBody"
        kkoMessage.kkoSubject = "" // 템플릿 구조 때문에, 현재는 사용하지 않음
        kkoMessage.receiverList = this.createReceiverList(data)

        return kkoMessage
    }

    private fun createReceiverList(data: AdminAlarm): MutableList<Receiver> {

        val receiverList: MutableList<Receiver> = mutableListOf()

        val dealInfo = data.dealInfo ?: DealInfo()
        val dealDetail = dealInfo.dealDetail ?: DealDetail()

        val mdInfo = dealInfo.mdInfo ?: PartnerMember()
        val amdInfo = dealInfo.amdInfo ?: PartnerMember()
        val companyInfo = dealInfo.companyInfo ?: CompanyInfo()

        var mdMobile = mdInfo.userMobile
        var amdMobile = amdInfo.userMobile
        var csMobile = companyInfo.csMobile
        var affMobile = companyInfo.affMobile
        var inventoryMobile = companyInfo.inventoryMobile

        val mannerFlag = dealDetail.orderMannerAlarmFlag
        val alarmTarget = dealDetail.orderFinishAlarmTarget.takeIf { it.isNotEmpty() } ?: "{}"
        val targetInfo = Gson().fromJson(alarmTarget, AdminAlarmTargetTemplate::class.java)

        val md = targetInfo.md
        val amd = targetInfo.amd
        val cs = targetInfo.cs
        val aff = targetInfo.aff
        val inventory = targetInfo.inventory

        mdMobile = AlarmHelper.trimMobile(mdMobile)
        amdMobile = AlarmHelper.trimMobile(amdMobile)
        csMobile = AlarmHelper.trimMobile(csMobile)
        affMobile = AlarmHelper.trimMobile(affMobile)
        inventoryMobile = AlarmHelper.trimMobile(inventoryMobile)

        // MD/AMD : 즉시 발송
        // 그 외 : 에티켓 모드에 따라 익일 오전 9시 1분 발송
        val nowDateTime = AlarmHelper.convertMsgDate(LocalDateTime.now())
        val reqDateTime = AlarmHelper.convertMsgDate(AlarmHelper.getReqDateTime(mannerFlag))

        when (md == 1 && mdMobile.isNotEmpty()) {
            true -> receiverList.add(Receiver(mobile = mdMobile, reqDate = nowDateTime))
        }
        when (amd == 1 && amdMobile.isNotEmpty()) {
            true -> receiverList.add(Receiver(mobile = amdMobile, reqDate = nowDateTime))
        }
        when (cs == 1 && csMobile.isNotEmpty()) {
            true -> receiverList.add(Receiver(mobile = csMobile, reqDate = reqDateTime))
        }
        when (aff == 1 && affMobile.isNotEmpty()) {
            true -> receiverList.add(Receiver(mobile = affMobile, reqDate = reqDateTime))
        }
        when (inventory == 1 && inventoryMobile.isNotEmpty()) {
            true -> receiverList.add(Receiver(mobile = inventoryMobile, reqDate = reqDateTime))
        }

        logger.info("====== [ORDER_COMPLETE_ALARM] =================== [DEAL_ID] ${dealInfo.dealId} =================== ")
        logger.info("====== [ORDER_COMPLETE_ALARM] ====== MD  ($md) ====== : ${String.format("%-11s", AlarmHelper.maskMobile(mdMobile))} / $nowDateTime")
        logger.info("====== [ORDER_COMPLETE_ALARM] ====== AMD ($amd) ====== : ${String.format("%-11s", AlarmHelper.maskMobile(amdMobile))} / $nowDateTime")
        logger.info("====== [ORDER_COMPLETE_ALARM] ====== CS  ($cs) ====== : ${String.format("%-11s", AlarmHelper.maskMobile(csMobile))} / $reqDateTime")
        logger.info("====== [ORDER_COMPLETE_ALARM] ====== AFF ($aff) ====== : ${String.format("%-11s", AlarmHelper.maskMobile(affMobile))} / $reqDateTime")
        logger.info("====== [ORDER_COMPLETE_ALARM] ====== INV ($inventory) ====== : ${String.format("%-11s", AlarmHelper.maskMobile(inventoryMobile))} / $reqDateTime")

        return receiverList
    }

    private fun convertMsgTemplate(data: AdminAlarm): AdminAlarmMsgTemplate {

        val template = adminAlarmMsgTemplate.copy()
        var messageBody = template.messageBody
        var messageSubject = template.messageSubject

        val dealInfo = data.dealInfo ?: DealInfo()
        val orderInfo = data.orderInfo ?: OrderInfo()

        var orderName = orderInfo.orderName
        var orderMobile = orderInfo.orderMobile

        var dealName = orderInfo.dealName
        var dealId = dealInfo.dealId.toString()

        val optionCouponList = data.optionCouponList
        val optionDate = AlarmHelper.convertOptionDate(optionCouponList)
        var optionId = if (optionCouponList.isEmpty()) "" else "${optionCouponList[0].optionId}"
        var optionName = if (optionCouponList.isEmpty()) "" else AlarmHelper.getOptionName(optionCouponList)
        val optionExtra = if (optionCouponList.size > 1) " 외 ${optionCouponList.size - 1}개" else ""

        orderName = AlarmHelper.convertText(orderName, template.orderNameMaxLength)
        orderMobile = AlarmHelper.convertMobile(orderMobile)
        dealName = AlarmHelper.convertText(dealName, template.dealNameMaxLength, ellipsis = true)
        dealId = AlarmHelper.convertText(dealId, template.dealIdMaxLength)
        optionName = AlarmHelper.convertText(optionName, template.optionNameMaxLength, ellipsis = true)
        optionId = AlarmHelper.convertText(optionId, template.optionIdMaxLength)

        dealId = if (dealId.isNotEmpty()) "($dealId)" else ""
        optionId = if (optionId.isNotEmpty()) "($optionId)" else ""
        optionName = if (optionName.isNotEmpty()) optionName else "없음"

        messageBody = messageBody.replace(oldValue = "{1}", newValue = optionDate)
        messageBody = messageBody.replace(oldValue = "{2}", newValue = orderName)
        messageBody = messageBody.replace(oldValue = "{3}", newValue = orderMobile)
        messageBody = messageBody.replace(oldValue = "{4}", newValue = dealName)
        messageBody = messageBody.replace(oldValue = "{5}", newValue = dealId)
        messageBody = messageBody.replace(oldValue = "{6}", newValue = optionName)
        messageBody = messageBody.replace(oldValue = "{7}", newValue = optionId)
        messageBody = messageBody.replace(oldValue = "{8}", newValue = optionExtra)

        messageSubject = messageSubject.replace(oldValue = "{0}", newValue = "구매")

        template.messageBody = messageBody
        template.messageSubject = messageSubject

        return template
    }
}