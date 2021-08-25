package me.eungi.photogallery

import android.app.Activity
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat

private const val TAG = "NotificationReceiver"

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "Received result: $resultCode")
        if (resultCode != Activity.RESULT_OK) {
            // 액티비티가 포그라운드에 있으므로 여기서 처리하지 않는다
            return
        }
        val requestCode = intent.getIntExtra(PollWorker.REQUEST_CODE, 0)
        val notification = intent.getParcelableExtra<Notification>(PollWorker.NOTIFICATION)!!

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(requestCode, notification)
    }
}