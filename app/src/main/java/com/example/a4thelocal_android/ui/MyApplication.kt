package com.example.a4thelocal_android.ui

import android.app.Application
import android.content.Context

class MyApplication: Application() {

    companion object {
        private var mContext: Context? = null
        fun getAppContext(): Context? {
            return mContext
        }
    }


    override fun onCreate() {
        super.onCreate()
        mContext = getApplicationContext();
    }


}