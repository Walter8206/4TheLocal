package com.example.a4thelocal_android.utills

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.a4thelocal_android.ui.MyApplication

class MyPreference {

    companion object {
        var shared = MyPreference()
        const val AppPreference = "4TLPreference"
        const val ProfileImagePreference = "ProfileImagePreference"
        const val isSchoolSelected = "schoolSelected"
        const val apiToken = "apiToken"
        const val userId = "cusId_key"
        const val isLoggedIn = "isLoggedIn-key"
    }

    lateinit var sharedPref : SharedPreferences

    fun saveBool( key: String?, value: Boolean?) {
        sharedPref = MyApplication.getAppContext()!!.getSharedPreferences(AppPreference , Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean(key, value!!).apply()
    }

    fun getBool(key: String?): Boolean? {
        sharedPref = MyApplication.getAppContext()!!.getSharedPreferences(AppPreference , Context.MODE_PRIVATE)
        return sharedPref.getBoolean(key, false)
    }


    fun saveString(key: String?, value: String?) {
        sharedPref = MyApplication.getAppContext()!!.getSharedPreferences(AppPreference , Context.MODE_PRIVATE)
        sharedPref.edit().putString(key, value).apply()
    }

    fun getString(key: String?): String? {
        sharedPref = MyApplication.getAppContext()!!.getSharedPreferences(AppPreference , Context.MODE_PRIVATE)
        return sharedPref.getString(key, null)
    }

    fun saveInt(key: String?, value: Int?) {
        sharedPref = MyApplication.getAppContext()!!.getSharedPreferences(AppPreference , Context.MODE_PRIVATE)
        value?.let { sharedPref.edit().putInt(key, it).apply() }
    }

    fun getInt(key: String?): Int{
        sharedPref = MyApplication.getAppContext()!!.getSharedPreferences(AppPreference , Context.MODE_PRIVATE)
        return sharedPref.getInt(key, 0)
    }


    fun saveImgString(key: String?, value: String?) {
        sharedPref = MyApplication.getAppContext()!!.getSharedPreferences(ProfileImagePreference , Context.MODE_PRIVATE)
        sharedPref.edit().putString(key, value).apply()
    }

    fun getImgString(key: String?): String? {
        sharedPref = MyApplication.getAppContext()!!.getSharedPreferences(ProfileImagePreference , Context.MODE_PRIVATE)
        return sharedPref.getString(key, null)
    }

    fun clearAll(){
        sharedPref = MyApplication.getAppContext()!!.getSharedPreferences(AppPreference , Context.MODE_PRIVATE)
        sharedPref.edit().clear().commit()
    }
}