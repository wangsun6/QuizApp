package com.wangsun.android.qaapp.utilities

import android.content.Context
import android.content.SharedPreferences



class UtilSharedPreference{

    companion object {
        lateinit var sp: SharedPreferences
        lateinit var editor: SharedPreferences.Editor
        private var instance: UtilSharedPreference? = null


        //CONSTANT
        private val IS_LOGIN = "isLogin"
        private val USER_TYPE = "userType"
        private val LOGGED_IN_EMAIL = "loggedInEmail"



        //values for userType
        val ADMIN = "admin"
        val MEMBER = "member"

        fun getInstance(context: Context): UtilSharedPreference? {
            if (instance == null) {
                instance = UtilSharedPreference()
                val SHARE_USER_INFO = "app_data";
                sp = context.getSharedPreferences(SHARE_USER_INFO, Context.MODE_PRIVATE)
                editor = sp.edit()
                editor.apply()
            }
            return instance
        }

        //************isLOGIN ?******************************
        fun setIsLoggedIn(context: Context,loggedInValue: Boolean) {
            getInstance(context)

            editor.putBoolean(IS_LOGIN, loggedInValue)
            editor.commit()
        }
        fun getIsLoggedIn(context: Context): Boolean {
            getInstance(context)
            return sp.getBoolean(IS_LOGIN,false)
        }

        //**********Check if its admin or member logged in*******************
        fun setUserType(context: Context,value: String) {
            getInstance(context)

            editor.putString(USER_TYPE, value)
            editor.commit()
        }
        fun getUserType(context: Context): String {
            getInstance(context)
            return sp.getString(USER_TYPE,"")!!
        }

        //************LOGIN user email******************************
        fun setLoggedInEmail(context: Context,value: String) {
            getInstance(context)

            editor.putString(LOGGED_IN_EMAIL, value)
            editor.commit()
        }
        fun getLoggedInEmail(context: Context): String {
            getInstance(context)

//            sp.getString(LOGGED_IN_EMAIL,"")?.let {
//                return it
//            }
            return sp.getString(LOGGED_IN_EMAIL,"")!!
        }

    }
}