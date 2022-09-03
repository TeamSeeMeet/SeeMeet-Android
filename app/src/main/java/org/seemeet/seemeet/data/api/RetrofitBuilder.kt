package org.seemeet.seemeet.data.api

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.seemeet.seemeet.data.AuthInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    const val BASE_URL = "http://3.34.126.253:3000/"

    private val authInterceptor : Interceptor = AuthInterceptor()

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val seeMeetRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
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