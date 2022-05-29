package com.example.a4thelocal_android.api.services

import android.content.Context
import android.net.ConnectivityManager

class NetworkService {


    companion object {
        fun isNetworkConnected(ctx: Context): Boolean {
            val connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager //1
            val networkInfo = connectivityManager.activeNetworkInfo //2
            return networkInfo != null && networkInfo.isConnected //3
        }
    }
}