package org.seemeet.seemeet.data.api

import org.seemeet.seemeet.data.model.response.plan.ResponseComePlanList
import org.seemeet.seemeet.data.model.response.plan.ResponseLastPlan
import org.seemeet.seemeet.data.model.response.plan.ResponsePlanResponseList
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface PlanService {

    @GET("plan/comeplan/{year}/{month}")
    suspend fun getComePlan(
        @Path("year") year : Int,
        @Path("month") month : Int,
        @Header("accesstoken") token : String
    ): ResponseComePlanList

    @GET("plan/lastplan/{year}/{month}/{day}")
    suspend fun getLastPlan(
        @Path("year") year : Int,
        @Path("month") month : Int,
        @Path("day") day : Int,
        @Header("accesstoken") token : String
    ): ResponseLastPlan

    @GET("plan/response/{dateId}")
    suspend fun getPlanResponse(
        @Path("dateId") dateId : Int,
        @Header("accesstoken") token : String
    ) : ResponsePlanResponseList
}