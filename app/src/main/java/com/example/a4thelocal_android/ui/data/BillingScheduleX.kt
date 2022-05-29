package com.example.a4thelocal_android.api.data

data class BillingScheduleX(
    val end_at: String,
    val interval: String,
    val next_payment_at: String,
    val period: String,
    val start_at: String,
    val trial_end_at: String
)