package com.example.a4thelocal_android.api.data

import com.example.a4thelocal_android.ui.data.ImageXX

data class AcfX(
    val address_1: String,
    val address_2: String,
    val business_cell_phone: String,
    val business_phone: String,
    val business_type: List<String>,
    val city: String,
    val county_name: String,
    val description: String,
    val discount: String,
    val facebook: String,
    val first_name: String,
    val image: Any,
    val instagram: String,
    val last_name: String,
    val map_location: MapLocationX,
    val permission_to_use_picture: Boolean,
    val state: String,
    val website: String,
    val zip_code: String
)