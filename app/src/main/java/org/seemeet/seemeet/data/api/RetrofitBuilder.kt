package org.seemeet.seemeet.data.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val BASE_URL = "https://asia-northeast3-seemeet-700c2.cloudfunctions.net/api/"

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val seeMeetRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val planService: PlanService = seeMeetRetrofit.create(PlanService::class.java)
    val friendService: FriendService = seeMeetRetrofit.create(FriendService::class.java)
    val invitationService : InvitationService = seeMeetRetrofit.create(InvitationService::class.java)
    val loginService : LoginService = seeMeetRetrofit.create(LoginService::class.java)
}