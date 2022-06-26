package org.seemeet.seemeet.data.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val BASE_URL = "http://3.34.126.253:3000/"

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    val seeMeetRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val planService: PlanService = seeMeetRetrofit.create(PlanService::class.java)
    val friendService: FriendService = seeMeetRetrofit.create(FriendService::class.java)
    val invitationService: InvitationService = seeMeetRetrofit.create(InvitationService::class.java)
    val calendarService: CalendarService = seeMeetRetrofit.create(CalendarService::class.java)
    val loginService: LoginService = seeMeetRetrofit.create(LoginService::class.java)
    val registerService: RegisterService = seeMeetRetrofit.create(RegisterService::class.java)
    val mypageService: MyPageService = seeMeetRetrofit.create(MyPageService::class.java)
    val withdrawalService: WithdrawalService = seeMeetRetrofit.create(WithdrawalService::class.java)
}