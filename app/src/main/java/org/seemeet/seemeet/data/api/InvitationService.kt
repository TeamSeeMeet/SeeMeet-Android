package org.seemeet.seemeet.data.api

import org.seemeet.seemeet.data.model.request.invitation.RequestSendInvitationConfirm
import org.seemeet.seemeet.data.model.response.invitation.ResponseInvitationList
import org.seemeet.seemeet.data.model.response.invitation.ResponseReceiveInvitation
import org.seemeet.seemeet.data.model.response.invitation.ResponseSendInvitation
import org.seemeet.seemeet.data.model.response.invitationResponse.ResponseYesInvitationResponse
import retrofit2.http.*
import com.google.gson.annotations.SerializedName
import org.seemeet.seemeet.data.model.request.invitationResponse.RequestInvitationResponse
import org.seemeet.seemeet.data.model.response.invitationResponse.ResponseNoInvitationResponse


interface InvitationService {

    @GET("invitation/list")
    suspend fun getAllInvitationList(
        @Header("accesstoken") token : String
    ) : ResponseInvitationList

    //보낸 요청 조회
    @GET("invitation/{invitationId}")
    suspend fun getSendInvitationData(
        @Path("invitationId") invitationId : Int,
        @Header("accesstoken") token: String
    ) : ResponseSendInvitation

    //보낸 요청 확정
    @POST("invitation/{invitationId}")
    suspend fun setConfirmSendInvitation(
        @Path("invitationId") invitationId : Int,
        @Body requestSendInvitationConfirm: RequestSendInvitationConfirm,
        @Header("accesstoken") token: String
    )

    //보낸 요청 취소
    @PUT("invitation/{invitationId}")
    suspend fun setCancelSendInvitation(
        @Path("invitationId") invitationId : Int,
        @Header("accesstoken") token: String
    )


    //받은 요청 조회
    @GET("invitation/{invitationId}")
    suspend fun getReceiveInvitationData(
        @Path("invitationId") invitationId : Int,
        @Header("accesstoken") token: String
    ) : ResponseReceiveInvitation

    //받은 요청 수락
    @POST("invitation-response/{invitationId}")
    suspend fun setYesReceiveInvitationResponse(
        @Path("invitationId") invitationId: Int,
        @Body invitationDateIds : RequestInvitationResponse,
        @Header("accesstoken") token : String
    ) : ResponseYesInvitationResponse

    //받은 요청 취소
    @POST("invitation-response/{invitationId}/reject")
    suspend fun setNoReceiveInvitationResponse(
        @Path("invitationId") invitationId: Int,
        @Header("accesstoken") token : String
    ) : ResponseNoInvitationResponse
}