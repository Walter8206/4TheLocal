package com.example.a4thelocal_android.api.data

data class CountiesResponse(
    val counties: List<String>,
    val state_code: String,
    val state_name: String
)