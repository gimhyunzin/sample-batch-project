package com.sample.batch.order.model.helper

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.sample.batch.common.property.BatchTemplate
import com.sample.batch.order.model.OptionCoupon
import com.sample.batch.order.model.template.AdminAlarmMsgTemplate
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.lang.Nullable
import java.io.File
import java.io.FileReader
import java.io.InputStream
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern

class AlarmHelper {
    companion object {

        private val logger = LoggerFactory.getLogger(AlarmHelper::class.java)

        private const val MANNER_OFF_START_TIME = "09:00:00"
        private const val MANNER_OFF_END_TIME = "20:59:59"

        private const val OPTION_SEPARATOR = "^"
        private const val MOBILE_SEPARATOR = "-"
        private const val MOBILE_PATTERN = "(\\d{3})(\\d{3,4})(\\d{4})"

        private const val OPTION_OLD_SEPARATOR = "^"
        private const val OPTION_NEW_SEPARATOR = "|"
        private const val OPTION_LINE_SEPARATOR = "<br/>"

        private const val MSG_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"
        private const val MAIL_DATE_PATTERN = "yyyy년 MM월 dd일 a hh시 mm분"

        /**
         * 글자 제한
         */
        fun convertText(data: String, targetMaxLength: Int, @Nullable ellipsis: Boolean = false): String {
            return when (targetMaxLength < data.length) {
                true -> data.substring(0, targetMaxLength + 1).plus(if (ellipsis) "..." else "")
                false -> data
            }
        }

        /**
         * 메일용 날짜
         */
        fun convertMailDate(date: Date?): String {
            return when (date) {
                null -> "없음"
                else -> SimpleDateFormat(MAIL_DATE_PATTERN).format(date)
            }
        }

        /**
         * 메세지용 날짜
         */
        fun convertMsgDate(dateTime: LocalDateTime): String {
            return dateTime.format(DateTimeFormatter.ofPattern(MSG_DATE_PATTERN))
        }

        /**
         * 휴대폰 형식
         */
        fun convertMobile(data: String): String {

            var mobile = AlarmHelper.trimMobile(data)

            when (Pattern.matches(MOBILE_PATTERN, mobile)) {
                true -> mobile = mobile.replace(MOBILE_PATTERN.toRegex(), "$1-$2-$3")
            }

            return mobile
        }

        /**
         * 사용일 by 옵션 리스트
         * >> 캘린더용 날짜 중, mix/max or 단일 or "없음"
         */
        fun convertOptionDate(optionCouponList: List<OptionCoupon>): String {

            val minDate = optionCouponList.minWith(compareBy { it.optionDate })?.optionDate ?: ""
            val maxDate = optionCouponList.maxWith(compareBy { it.optionDate })?.optionDate ?: ""

            return when {
                minDate.isNotEmpty() && maxDate.isNotEmpty() -> if (minDate == maxDate) minDate else "$minDate~$maxDate"
                minDate.isNotEmpty() && maxDate.isEmpty() -> minDate
                minDate.isEmpty() && maxDate.isNotEmpty() -> maxDate
                else -> "없음"
            }
        }

        /**
         * 첫번째 옵션 by 옵션 리스트
         */
        fun getOptionName(optionCouponList: List<OptionCoupon>): String {
            return optionCouponList[0].optionValue.split(OPTION_SEPARATOR)[0]
        }

        /**
         * 전체 옵션 by 옵션 리스트
         */
        fun getFullOptionString(optionCouponList: List<OptionCoupon>): String {

            return optionCouponList
                    .joinToString(OPTION_LINE_SEPARATOR) {
                        it.optionValue
                                .replace(OPTION_OLD_SEPARATOR, " $OPTION_NEW_SEPARATOR ")
                                .plus(
                                        when {
                                            it.couponCode.isEmpty() -> ""
                                            else -> " $OPTION_NEW_SEPARATOR (${it.couponCode})"
                                        }
                                )
                    }
        }

        /**
         * 예약 발송 일시
         */
        fun getReqDateTime(mannerFlag : Int): LocalDateTime {

            var reqDateTime = LocalDateTime.now()

            val mannerOffStart = Time.valueOf(MANNER_OFF_START_TIME)
            val mannerOffEnd = Time.valueOf(MANNER_OFF_END_TIME)
            val mannerOffRange = mannerOffStart..mannerOffEnd
            val standardTime = Time.valueOf("11:59:59")

            val nowDateTime = LocalDateTime.now()
            val nowTime = Time.valueOf("${nowDateTime.hour}:${nowDateTime.minute}:${nowDateTime.second}")

            val isMannerMode = mannerFlag == 1
            val isMannerOffRange = nowTime in mannerOffRange

            // 에티켓 모드 설정 && 정상 발송 시간대 아닌 경우
            when (isMannerMode && !isMannerOffRange) {
                true -> {
                    // 오후 일 경우, 익일로 변경
                    when (standardTime < nowTime) {
                        true -> reqDateTime = reqDateTime.plusDays(1)
                    }

                    // 오전 9시 1분 으로 설정
                    reqDateTime = reqDateTime.with(LocalTime.of(9, 1))
                }
            }

            logger.debug("========> MANNER_MODE      : [$isMannerMode]")
            logger.debug("========> MANNER OFF RANGE : [$isMannerOffRange] ( [$nowTime] BETWEEN [$mannerOffStart~$mannerOffEnd] )")
            logger.debug("========> NOW DATETIME     : [${convertMsgDate(nowDateTime)}]")
            logger.debug("========> REQ DATETIME     : [${convertMsgDate(reqDateTime)}]")

            return reqDateTime
        }

        /**
         * 이름 마스킹
         * > 김*진
         */
        fun maskName(data : String): String {
            val name = data.trim()

            return name.withIndex().joinToString("") {
                when {
                    (it.index + 1) % 2 == 0 -> "*"
                    else -> it.value.toString()
                }
            }
        }

        /**
         * 휴대폰 마스킹
         * > 010****1234
         */
        fun maskMobile(data: String): String {
            val mobile = AlarmHelper.trimMobile(data)

            return mobile.withIndex().joinToString("") {
                when {
                    (it.index + 1) in 4..7 -> "*"
                    else -> it.value.toString()
                }
            }
        }

        /**
         * 이메일 마스킹
         * > abc***e@abc.com
         */
        fun maskEmail(data: String): String {
            val email = data.trim()

            return when {
                email.isEmpty() -> email
                else -> email.replace("(?<=.{${email.indexOf("@") / 2}}).(?=.*@)".toRegex(), "*")
            }
        }

        /**
         * 휴대폰 TRIM
         */
        fun trimMobile(data: String): String {
            return data.trim().replace(MOBILE_SEPARATOR, "")
        }

        /**
         * MSG 템플릿
         */
        fun createMsgTemplate(): AdminAlarmMsgTemplate {

            val template: AdminAlarmMsgTemplate
            val classPathResource = ClassPathResource(BatchTemplate.ORDER_ADMIN_ALARM_MSG_PATH.value)
            val inputStream: InputStream = classPathResource.inputStream
            val tempFile: File = File.createTempFile("temp", ".json")

            try {
                FileUtils.copyInputStreamToFile(inputStream, tempFile)
                template = Gson().fromJson(JsonReader(FileReader(tempFile)), AdminAlarmMsgTemplate::class.java)

            } finally {
                IOUtils.closeQuietly(inputStream)
            }
            return template
        }
    }
}