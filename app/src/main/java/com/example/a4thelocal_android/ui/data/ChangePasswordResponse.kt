package com.example.a4thelocal_android.api.data

data class ChangePasswordResponse(
    val billing: Billing,
    val date_created: String,
    val date_created_gmt: String,
    val date_modified: String,
    val date_modified_gmt: String,
    val email: String,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val role: String,
    val username: String
)