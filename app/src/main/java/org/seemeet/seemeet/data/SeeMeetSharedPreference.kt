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
    private const val ACCESS_TOKEN = "ACCESS_TOKEN"
    private const val REFRESH_TOKEN = "REFRESH_TOKEN"
    private const val USER_EMAIL = "USER_EMAIL"
    private const val IS_SOCIAL_LOGIN = "IS_SOCIAL_LOGIN"
    private const val USER_PROFILE = "USER_PROFILE"
    private const val PUSH_ON = "PUSH_ON"


    fun init(context: Context) {
        preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    fun setUserId(value: String) {
        preferences.edit {
            putString(USER_ID, value)
            apply()
        }
    }

    fun setUserName(value: String) {
        preferences.edit {
            putString(USER_NAME, value)
            apply()
        }
    }

    fun setUserEmail(value: String) {
        preferences.edit {
            putString(USER_EMAIL, value)
            apply()
        }
    }

    fun getUserName(): String? = preferences.getString(USER_NAME, "로그인")

    fun getUserEmail(): String? = preferences.getString(USER_EMAIL, "SeeMeet에서 친구와 약속을 잡아보세요!")

    fun getUserId(): String? = preferences.getString(USER_ID, "SeeMeet에서 친구와 약속을 잡아보세요!")

    fun setUserFb(value : String){
        preferences.edit{
            putString(USER_FB, value)
            apply()
        }
    }

    fun getUserFb(): String = preferences.getString(USER_FB, "") ?: ""

<<<<<<< HEAD
    fun setToken(accessToken: String, refreshToken : String) {
=======
    fun setToken(accessToken: String, refreshToken: String) {
>>>>>>> develop
        preferences.edit {
            putString(ACCESS_TOKEN, accessToken)
            putString(REFRESH_TOKEN, refreshToken)
            apply()
        }
    }

    fun getToken(): String = preferences.getString(ACCESS_TOKEN, "") ?: ""
<<<<<<< HEAD

    fun getRefreshToken(): String = preferences.getString(REFRESH_TOKEN, "") ?: ""
=======
>>>>>>> develop

    fun setLogin(value: Boolean) {
        preferences.edit {
            putBoolean(IS_LOGIN, value)
            apply()
        }
    }

    fun getLogin(): Boolean = preferences.getBoolean(IS_LOGIN, false)

    fun setSocialLogin(value: Boolean) {
        preferences.edit {
            putBoolean(IS_SOCIAL_LOGIN, value)
            apply()
        }
    }

    fun getSocialLogin(): Boolean = preferences.getBoolean(IS_SOCIAL_LOGIN, false)

    fun setUserProfile(value: String?) {
        preferences.edit {
            putString(USER_PROFILE, value)
            apply()
        }
    }

    fun getUserProfile(): String? = preferences.getString(USER_PROFILE, "")
    fun clearStorage(): Boolean {
        return preferences.edit().clear().commit()
    }

    fun setPushOn(value: Boolean) {
        preferences.edit {
            putBoolean(PUSH_ON, value)
            apply()
        }
    }

    fun getPushOn(): Boolean = preferences.getBoolean(PUSH_ON, false)
}


