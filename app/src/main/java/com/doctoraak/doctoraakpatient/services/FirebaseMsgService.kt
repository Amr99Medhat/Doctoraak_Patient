package com.doctoraak.doctoraakpatient.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.FirebaseTokenResponse
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.repository.remote.ApiOthers
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener
import com.doctoraak.doctoraakpatient.ui.notification.NotificationActivity
import com.doctoraak.doctoraakpatient.utils.Utils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMsgService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        var titleEn = ""
        var titeleAr = ""
        var bodyEn = ""
        var bodyAr = ""

        if (remoteMessage.data != null) {
             titleEn = remoteMessage.data["title_en"]!!
             titeleAr = remoteMessage.data["title_ar"]!!
             bodyEn = remoteMessage.data["body_en"]!!
             bodyAr = remoteMessage.data["body_ar"]!!
        }

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager = NotificationManagerCompat.from(this)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            val notificationChannel = NotificationChannel(
                "my_notification", "n_channel", NotificationManager.IMPORTANCE_MAX
            )
            notificationChannel.description = "description"
            notificationChannel.name = "Channel Name"
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val mBuilder = NotificationCompat.Builder(applicationContext,
            getString(R.string.notification_channel_id))
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(Utils.getTextForAppLanguage(titleEn , titeleAr , titleEn))
            .setContentText(Utils.getTextForAppLanguage(bodyEn , bodyAr , bodyEn))
            .setSound(alarmSound)
            .setChannelId("my_notification")
            .setStyle(NotificationCompat.BigTextStyle().bigText(Utils.getTextForAppLanguage(bodyEn , bodyAr , bodyEn)))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)


        val intent = Intent(this , NotificationActivity::class.java)

        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        mBuilder.setContentIntent(pendingIntent)

        val notificationId = System.currentTimeMillis().toInt()

        notificationManager.notify(notificationId, mBuilder.build())

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        if (SessionManager.isLogIn()) {
            ApiOthers.updateFirebaseToken(token, Utils.getUserId()
                , "PATIENT", Utils.getApiToken(), object :
                    BaseResponseListener<FirebaseTokenResponse> {
                    override fun onSuccess(model: FirebaseTokenResponse) {
                    }

                    override fun onLoading() {
                    }

                    override fun onErrorMsg(errorMsg: String) {
                    }

                    override fun onError(errorMsgId: Int) {
                    }
                })
        }
    }
}