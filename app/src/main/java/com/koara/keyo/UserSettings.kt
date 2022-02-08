package com.koara.keyo

import android.content.Context
import android.content.SharedPreferences

class UserSettings(context: Context) {
    private val sharedPreferences : SharedPreferences = context.getSharedPreferences("keyo",
        Context.MODE_PRIVATE)

    //Turn dark mode on and off
    fun setDarkModeState(state : Boolean?){
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("Dark",state!!)
        editor.apply()
    }
    //Checks if dark mode is enabled or not
    fun loadDarkModeState(): Boolean {
        return sharedPreferences.getBoolean("Dark", false)
    }

    //Turn lock mode on and off
    fun setLockState(state : Boolean?){
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("Lock",state!!)
        editor.apply()
    }

    //Checks if lock mode is enabled or not
    fun loadLockState(): Boolean {
        return sharedPreferences.getBoolean("Lock", false)
    }
}