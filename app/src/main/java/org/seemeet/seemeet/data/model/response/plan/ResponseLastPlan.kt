package org.seemeet.seemeet.data.model.response.plan
import com.google.gson.annotations.SerializedName


data class ResponseLastPlan(
    @SerializedName("data")
    val `data`: LastPlanData,
    @SerializedName("status")
    val status: Int,
    @SerializedName("success")
    val success: Boolean
)

data class LastPlanData(
    @SerializedName("date")
    val date: String
)