package com.example.a4thelocal_android.api.data

data class LocationResponseItem(
    val acf: AcfX,
    val content: ContentX,
    val date: String,
    val date_gmt: String,
    val excerpt: ExcerptX,
    val featured_media: Int,
    val id: Int,
    val link: String,
    val menu_order: Int,
    val parent: Int,
    val slug: String,
    val status: String,
    val template: String,
    val title: TitleX,
    val type: String
)