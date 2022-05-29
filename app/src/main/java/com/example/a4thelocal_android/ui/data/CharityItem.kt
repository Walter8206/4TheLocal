package com.example.a4thelocal_android.api.data

data class CharityItem(
//    val _links: LinksXX,
    val acf: AcfXX,
    val content: ContentXX,
    val date: String,
    val date_gmt: String,
    val featured_media: Int,
    val guid: GuidX,
    val id: Int,
    val link: String,
    val meta: Meta,
    val modified: String,
    val modified_gmt: String,
    val slug: String,
    val status: String,
    val template: String,
    val title: TitleXX,
    val type: String,
    val yoast_head: String,
    val yoast_head_json: YoastHeadJson
)