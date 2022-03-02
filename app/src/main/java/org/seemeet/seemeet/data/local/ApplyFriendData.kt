package org.seemeet.seemeet.data.local

import java.io.Serializable

data class ApplyFriendData( //첫번째 약속신청 뷰에서 두번째로 넘길 때 주려고 로컬로 데이터 클래스 만듦
    val id: Int,
    val username: String
) : Serializable
