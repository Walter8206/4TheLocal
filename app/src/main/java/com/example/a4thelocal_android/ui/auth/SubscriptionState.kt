package com.example.a4thelocal_android.ui.auth

import com.example.a4thelocal_android.api.data.AllProductResponseItem
import com.example.a4thelocal_android.api.data.CharityItem

class SubscriptionState {

    companion object{

        val selectedProducts = ArrayList<AllProductResponseItem>()
        val selectedCharities = ArrayList<CharityItem>()
        var boosterState:String? = null
        var boosterCounty:String? = null
        var boosterSchool:String? = null
        var boosters = ArrayList<String>()
        var boosterStudents = ArrayList<String>()

        var billing_firstName = ""
        var billing_lastName = ""
        var billing_companyName = ""
        var billing_county = ""
        var billing_street_address = ""
        var billing_city = ""
        var billing_state = ""
        var billing_zip = ""
        var billing_phone = ""
        var billing_email = ""
        var billing_password = ""
        var billing_note = ""
        var billing_period = "month"
        var billing_interval = 1


        fun clearAll(){
            selectedProducts.clear()
            boosterState = null
            boosterCounty = null
            boosterSchool = null
            boosters.clear()
            boosterStudents.clear()
        }
    }
}