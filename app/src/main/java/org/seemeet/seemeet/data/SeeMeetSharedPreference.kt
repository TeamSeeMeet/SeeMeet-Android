package org.seemeet.seemeet.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SeeMeetSharedPreference {
    lateinit var preferences: SharedPreferences

    private const val USER_ID = "USER_ID"
    private const val USER_NAME = "USER_NAME"
    private const val IS_LOGIN = "IS_LOGIN"
    private const val USER_FB = "USER_FB"
    private const val TOKEN = "TOKEN"
    private const val USER_EMAIL = "USER_EMAIL"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    fun setUserId(value : Int){
        preferences.edit{
            putInt(USER_ID, value)
        }
    }

    fun setUserName(value : String){
        preferences.edit{
            putString(USER_NAME, value)
        }
    }

    fun setUserEmail(value : String){
        preferences.edit{
            putString(USER_EMAIL, value)
        }
    }

    fun getUserName(): String? = preferences.getString(USER_NAME,"로그인")

    fun getUserEmail(): String? = preferences.getString(USER_EMAIL, "SeeMeet에서 친구와 약속을 잡아보세요!")

    fun getUserId(): Int= preferences.getInt(USER_ID, -1)

    fun setUserFb(value : String){
        preferences.edit{
            putString(USER_ID, value)
        }
    }

    fun getUserFb(): String = preferences.getString(USER_FB, "") ?: ""

    fun setToken(value : String){
        preferences.edit{
            putString(TOKEN, value)
        }
    }

    fun getToken(): String = preferences.getString(TOKEN, "") ?: ""


    fun setLogin(value: Boolean) {
        preferences.edit{
            putBoolean(IS_LOGIN, value)
        }
    }

    fun getLogin(): Boolean = preferences.getBoolean(IS_LOGIN, false)


    fun clearStorage() {
        preferences.edit().clear().commit()
    }
}


