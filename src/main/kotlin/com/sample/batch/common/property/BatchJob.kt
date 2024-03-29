package com.sample.batch.common.property

enum class BatchJob(val value: String) {

    // job name
    TICKET_JOB_NAME("TICKET_ALARM_JOB"),
    CLAIM_REQUEST_ALARM_JOB_NAME("CLAIM_REQUEST_ALARM_JOB"),
    CLAIM_COMPLETE_ALARM_JOB_NAME("CLAIM_COMPLETE_ALARM_JOB"),
    ORDER_COMPLETE_ALARM_JOB_NAME("ORDER_COMPLETE_ALARM_JOB"),

    // step name
    TICKET_SMS_STEP_NAME("TICKET_SMS_STEP"),
    TICKET_MAIL_STEP_NAME("TICKET_MAIL_STEP"),

    CLAIM_REQUEST_ALARM_KKO_STEP("CLAIM_REQUEST_ALARM_KKO_STEP"),
    CLAIM_REQUEST_ALARM_LMS_STEP("CLAIM_REQUEST_ALARM_LMS_STEP"),
    CLAIM_REQUEST_ALARM_MAIL_STEP("CLAIM_REQUEST_ALARM_MAIL_STEP"),
    CLAIM_REQUEST_ALARM_KEY_STEP("CLAIM_REQUEST_ALARM_KEY_STEP"),

    CLAIM_COMPLETE_ALARM_KKO_STEP("CLAIM_COMPLETE_ALARM_KKO_STEP"),
    CLAIM_COMPLETE_ALARM_LMS_STEP("CLAIM_COMPLETE_ALARM_LMS_STEP"),
    CLAIM_COMPLETE_ALARM_MAIL_STEP("CLAIM_COMPLETE_ALARM_MAIL_STEP"),
    CLAIM_COMPLETE_ALARM_KEY_STEP("CLAIM_COMPLETE_ALARM_KEY_STEP"),

    ORDER_COMPLETE_ALARM_KKO_STEP("ORDER_COMPLETE_ALARM_KKO_STEP"),
    ORDER_COMPLETE_ALARM_LMS_STEP("ORDER_COMPLETE_ALARM_LMS_STEP"),
    ORDER_COMPLETE_ALARM_MAIL_STEP("ORDER_COMPLETE_ALARM_MAIL_STEP"),
    ORDER_COMPLETE_ALARM_KEY_STEP("ORDER_COMPLETE_ALARM_KEY_STEP")
}