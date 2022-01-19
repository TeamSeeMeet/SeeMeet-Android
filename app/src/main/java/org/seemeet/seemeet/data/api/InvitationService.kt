package org.seemeet.seemeet.data.api

import org.seemeet.seemeet.data.model.response.invitation.ResponseInvitationList
import retrofit2.http.GET
import retrofit2.http.Header

interface InvitationService {

    @GET("invitation/list")
    suspend fun getAllInvitationList(
        @Header("accesstoken") token : String
    ) : ResponseInvitationList

}