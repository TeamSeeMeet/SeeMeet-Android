package org.seemeet.seemeet.util

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        //여기서 생성되거나 갱신된 token 서버에 알리기.
        Log.d("******firebaseToken_FCMS", token)
    }

    //핸들러 클래스 하나 더 만들어서 아래 처럼 토큰 관리하자...
    /*
        로그인시 _ 활성화
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        로그아웃 시  _ 비활성화
        FirebaseMessaging.getInstance().setAutoInitEnabled(false);

    */

}