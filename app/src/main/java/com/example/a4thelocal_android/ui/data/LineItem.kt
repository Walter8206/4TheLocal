package com.example.a4thelocal_android.api.data

data class LineItem(
    val id: Int,
    val meta: List<Any>,
    val name: String,
    val price: String,
    val product_id: Int,
    val quantity: Int,
    val sku: String,
    val subtotal: String,
    val subtotal_tax: String,
    val tax_class: String,
    val total: String,
    val total_tax: String
)