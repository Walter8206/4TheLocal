package com.example.a4thelocal_android.api.data

data class Image(
    val ID: Int,
    val alt: String,
    val author: String,
    val caption: String,
    val date: String,
    val description: String,
    val filename: String,
    val filesize: Int,
    val height: Int,
    val icon: String,
    val id: Int,
    val link: String,
    val menu_order: Int,
    val mime_type: String,
    val modified: String,
    val name: String,
//    val sizes: Sizes,
    val status: String,
    val subtype: String,
    val title: String,
    val type: String,
    val uploaded_to: Int,
    val url: String,
    val width: Int
)