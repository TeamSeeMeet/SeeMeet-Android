package org.seemeet.seemeet.data.model.response.plan
import com.google.gson.annotations.SerializedName


data class ResponsePlanResponseList(
    @SerializedName("data")
    val `data`: List<PlanResponseData>,
    @SerializedName("status")
    val status: Int,
    @SerializedName("success")
    val success: Boolean
)

data class PlanResponseData(
    @SerializedName("date")
    val date: String,
    @SerializedName("end")
    val end: String,
    @SerializedName("invitationTitle")
    val invitationTitle: String,
    @SerializedName("planid")
    val planid: Int,
    @SerializedName("start")
    val start: String,
    @SerializedName("users")
    val users: List<PlanResponseUser>
)

data class PlanResponseUser(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("username")
    val username: String
)