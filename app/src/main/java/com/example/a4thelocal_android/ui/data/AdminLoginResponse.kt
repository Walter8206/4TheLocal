package com.example.a4thelocal_android.api.data

data class AdminLoginResponse(
    val avatar: String,
    val id: String,
    val token: String,
    val user_display_name: String,
    val user_email: String,
    val user_id: Int,
    val user_nicename: String,
    val user_role: List<String>
)