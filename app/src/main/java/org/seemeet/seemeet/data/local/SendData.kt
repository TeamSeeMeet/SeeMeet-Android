package org.seemeet.seemeet.data.local

data class SendData (
    val title : String,
    val desc : String,
    val isConfirmed : Boolean,
    val guests : List<GuestData>,
    val inviDates : List<InviData>
)
//칩그룹용
data class GuestData(
    val id : Int,
    val username : String,
    val isResponse : Boolean
)
data class InviData(
    val id : Int,
    val date : String,
    val time : String,
    val respondent : List<UserData>
)
//초대장 내 표시용
data class UserData(
    val name : String,
    val id : Int
)