package org.seemeet.seemeet.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.ui.main.MainActivity
import org.seemeet.seemeet.ui.receive.ReceiveActivity
import org.seemeet.seemeet.ui.send.SendActivity

class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("FirebaseMessagingService*************", "Message data : ${message.data}")

        if(message.data.isNotEmpty()){
            sendDataMessage(message.data)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        //여기서 생성되거나 갱신된 token 서버에 알리기.
        Log.d("******firebaseToken_FCMS", token)

        SeeMeetSharedPreference.setUserFb(token)
    }

    private fun sendRegistrationToServer (token : String?){

    }

    private fun sendDataMessage(data : MutableMap<String, String>){
        var intent = Intent(this, MainActivity::class.java)

        if(data["title"].isNullOrBlank())
            data["title"] = "SeeMeet"

        data["id"]?.let {
            if( "약속" in data["title"]!!){
                intent = Intent(this, ReceiveActivity::class.java)
                intent.putExtra("invitationId", data["id"]!!.toInt())
            } else {
                intent = Intent(this, SendActivity::class.java)
                intent.putExtra("invitationId", data["id"]!!.toInt())
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        } ?: let {
            intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("pushPlan", true)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, (System.currentTimeMillis()/1000).toInt(), intent, PendingIntent.FLAG_IMMUTABLE)

        val groupId = "group1"

        createNotificationChannel()

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        notificationBuilder.setContentTitle(data["title"]) //제목
            .setContentText(data["body"]) //메세지
            .setSmallIcon(R.drawable.ic_logo_push)
            .setContentIntent(pendingIntent) //이동 장소.
            .setAutoCancel(true)
            .setGroup(groupId)

        notificationBuilder.setFullScreenIntent(pendingIntent, true)
        notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH

        NotificationManagerCompat.from(this).notify((System.currentTimeMillis()/1000).toInt(), notificationBuilder.build() )

    }

    //채널 관리 : oreo 버전 이상의 경우, 채널별로 관리
    // 즉, noti 여러개 보낼 경우, 각각 쌓이는게 아니라 앱별로 그룹을 지어서 묶이게 됨.
    private fun createNotificationChannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.enableLights(true)
            channel.enableVibration(true)

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_NAME = "FCM SEEMEET"
        private const val CHANNEL_ID = "FCM_CHANNEL_ID"

    }

}