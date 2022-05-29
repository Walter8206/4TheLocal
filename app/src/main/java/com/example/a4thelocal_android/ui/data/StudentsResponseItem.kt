package com.example.a4thelocal_android.api.data

data class StudentsResponseItem(
    val booster: String,
    val county_name: String,
    val state: String,
    val students: List<Student>
)