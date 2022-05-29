package com.example.a4thelocal_android.utills

class Constants {

    companion object {
        var BASE_URL = ""
        var customer_key = ""
        var customer_secret = ""
        const val businessSignupLink = "https://4thelocal.com/signup-your-business/"
        const val schoolSignupLink = "https://4thelocalschools.com/school-signup/"
        const val allProducts = "/wc-api/v3/products"
        var privacyLink = "privacy-policy/"
        var supportLink = "support/"
        const val facebookLink = "https://www.facebook.com/4TheLocal/"
        const val instagramLink = "https://www.instagram.com/4thelocal/"

        const val annually_local = "Digital ID (a)"
        const val quarterly_local = "Digital ID (q)"
        const val monthly_local = "Digital ID (m)"
        const val membership_local = "Membership with Coupon"
        const val part_time_local = "PT Resident Digital ID"
        const val annual_scl = "Digital ID"
        const val localCreateMsg = "Thank you for your purchase & support! Please allow us to verify your billing address and activate your account."
        const val sclCreateMsg = "Thank you for your purchase & support!"
        const val couponMsg = "Coupon code already applied!"

        var adminUser = ""
        var adminPass = ""

        fun setSclConfig(){
            BASE_URL = "https://4thelocalschools.com/"
            adminUser = "rifat.nsu@gmail.com"
            adminPass = "Dhaka@123456"
            customer_key = "ck_7c5d6233eb917fe09fb4feadfd47b1891d3ea456"
            customer_secret = "cs_24c6843a7d8d97b9c32f57659ce50e56bc2f55c2"
        }

        fun setLocalConfig(){
            BASE_URL = "https://4thelocal.com/"
            adminUser = "rifat.nsu@gmail.com"
            adminPass = "is7Nim&j(oig^a4(o!rK^E@c"
            customer_key = "ck_3368dad2e5efcaf3e21ece7475a4da0a27a78a0c"
            customer_secret = "cs_23746813cb6e1f4ade90b9007bcc30b2210d8e21"
        }
    }
}