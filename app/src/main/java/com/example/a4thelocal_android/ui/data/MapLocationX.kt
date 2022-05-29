package com.example.a4thelocal_android.api.data

data class MapLocationX(
    val address: String,
    val city: String,
    val country: String,
    val country_short: String,
    val lat: Double,
    val lng: Double,
    val name: String,
    val place_id: String,
    val post_code: String,
    val state: String,
    val state_short: String,
    val street_name: String,
    val street_name_short: String,
    val street_number: String,
    val zoom: Int
)