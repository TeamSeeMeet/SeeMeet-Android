package org.seemeet.seemeet.data.model.response.plan
import com.google.gson.annotations.SerializedName


data class ResponsePlanDetail(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: Int,
    @SerializedName("success")
    val success: Boolean
)

data class Data(
    @SerializedName("date")
    val date: String,
    @SerializedName("end")
    val end: String,
    @SerializedName("hostname")
    val hostname: String,
    @SerializedName("impossible")
    val impossible: List<Any>,
    @SerializedName("invitationDesc")
    val invitationDesc: String,
    @SerializedName("invitationTitle")
    val invitationTitle: String,
    @SerializedName("invitationid")
    val invitationid: Int,
    @SerializedName("planid")
    val planid: Int,
    @SerializedName("possible")
    val possible: List<Possible>,
    @SerializedName("start")
    val start: String
)

data class Possible(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("username")
    val username: String
)