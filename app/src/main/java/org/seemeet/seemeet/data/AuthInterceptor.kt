package org.seemeet.seemeet.data

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.response.login.ResponsePostRefreshToken

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        if(response.code == UNAUTHORIZED_CODE && !isAuth(originalRequest)){
            val refreshTokenRequest = originalRequest.newBuilder().get()
                .url("${RetrofitBuilder.BASE_URL}auth/refresh")
                .addHeader("accesstoken",SeeMeetSharedPreference.getToken())
                .addHeader("refreshtoken",SeeMeetSharedPreference.getRefreshToken())
                .build()
            val refreshTokenResponse = chain.proceed(refreshTokenRequest)

            if(refreshTokenResponse.isSuccessful) {
                val gson = GsonBuilder().setLenient().create()
                val refreshToken = gson.fromJson(
                    refreshTokenResponse.body?.string(),
                    ResponsePostRefreshToken::class.java
                )
                with(SeeMeetSharedPreference) {
                    setToken(refreshToken.data.accessToken,refreshToken.data.refreshToken)
                }
                val request = originalRequest.newBuilder()
                    .addHeader("accesstoken",SeeMeetSharedPreference.getToken())
                    .build()
                return chain.proceed(request)
            }
        }
        return response
    }

    private fun isAuth(originalRequest: Request) : Boolean {
        return if(originalRequest.url.encodedPath.contains("withdrawal"))
            false
        else originalRequest.url.encodedPath.contains("auth")
    }

    companion object {
        private const val UNAUTHORIZED_CODE = 401
    }
}