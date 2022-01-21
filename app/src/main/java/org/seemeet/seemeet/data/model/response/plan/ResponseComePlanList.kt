package org.seemeet.seemeet.data.model.response.plan


import com.google.gson.annotations.SerializedName

//다가오는 약속
//이거 불확실함. _ 다시하기.
data class ResponseComePlanList(
    @SerializedName("data")
    val `data`: List<ComePlanData>,
    @SerializedName("status")
    val status: Int,
    @SerializedName("success")
    val success: Boolean
)

data class ComePlanData(
    @SerializedName("count")
    val count: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("invitationTitle")
    val invitationTitle: String,
    @SerializedName("planId")
    val planId: Int
)