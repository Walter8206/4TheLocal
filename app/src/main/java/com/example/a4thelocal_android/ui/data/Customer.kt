package com.example.a4thelocal_android.api.data

data class Customer(
    val avatar_url: String,
    val billing_address: BillingAddress,
    val created_at: String,
    val email: String,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val last_order_date: String,
    val last_order_id: Int,
    val last_update: String,
    val orders_count: Int,
    val role: String,
    val shipping_address: ShippingAddress,
    val total_spent: String,
    val username: String
)