package com.example.trailsurfing.common

import android.app.*
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import androidx.core.app.NotificationCompat
import com.example.trailsurfing.R
import com.example.trailsurfing.RouteService

class Utils {
    companion object {
        const val ID_CHANNEL: String = "LocationServiceСhannel"
        const val CURRENT_NOTIFICATION_ID: Int = 101
        const val EXTRA_REPEAL_LOCATION_TRACKING_FROM_NOTIFICATION =
            "${ContactsContract.Directory.PACKAGE_NAME}.extra.REPEAL_LOCATION_TRACKING_FROM_NOTIFICATION"

        fun generatingNotification(context: Context, text: String): Notification {
            val manager =
                context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                manager.createNotificationChannel(
                    NotificationChannel(
                        ID_CHANNEL,
                        "Служба определения местоположения",
                        NotificationManager.IMPORTANCE_DEFAULT
                    ) // Создание уведомления
                )
            }

            val repealIntent = Intent(
                context,
                RouteService::class.java
            )
            repealIntent.putExtra(EXTRA_REPEAL_LOCATION_TRACKING_FROM_NOTIFICATION, true)

            val stopService = PendingIntent.getService(
                context,
                0,
                repealIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            ) //Остановка сервиса

            val builder = NotificationCompat.Builder(context, ID_CHANNEL)
            return builder.apply {
                setContentTitle("Bike Route")
                setOngoing(true)
                addAction(R.drawable.trail_48px, "Остановить", stopService)
                setContentText("Производим запись вашего пути")
                setSmallIcon(R.drawable.trail_48px)
                setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            }.build() // Создание шаблона уведомления записи местоположения пользователя
        }

        fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
            val activitiManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (serviceInfo in activitiManager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceClass.name == serviceInfo.service.className) return true
            }
            return false
        }
    }
}
