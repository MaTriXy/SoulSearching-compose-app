package com.github.soulsearching.classes.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.github.soulsearching.MainActivity
import com.github.soulsearching.R
import com.github.soulsearching.classes.PlayerUtils
import com.github.soulsearching.classes.notification.receivers.DeletedNotificationIntentReceiver
import com.github.soulsearching.classes.player.SoulSearchingMediaPlayerImpl

abstract class SoulSearchingNotification(
    protected val context: Context,
    protected val mediaSessionToken: MediaSessionCompat.Token
) {
    protected val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    protected val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
        context,
        MUSIC_NOTIFICATION_CHANNEL_ID
    )
    protected lateinit var notification: Notification

    protected abstract val broadcastReceiver: BroadcastReceiver

    private val activityPendingIntent: PendingIntent = PendingIntent.getActivity(
        context,
        1,
        Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
        },
        PendingIntent.FLAG_IMMUTABLE
    )

    private val deleteNotificationIntent: PendingIntent = PendingIntent.getBroadcast(
        context,
        5,
        Intent(context, DeletedNotificationIntentReceiver::class.java),
        PendingIntent.FLAG_IMMUTABLE
    )

    fun initializeNotification() {
        context.registerReceiver(
            broadcastReceiver,
            IntentFilter(SoulSearchingMediaPlayerImpl.BROADCAST_NOTIFICATION)
        )

        notificationBuilder
            .setSmallIcon(R.drawable.ic_saxophone_svg)
            .setContentTitle(if (PlayerUtils.playerViewModel.currentMusic != null) PlayerUtils.playerViewModel.currentMusic?.name else "")
            .setContentText(if (PlayerUtils.playerViewModel.currentMusic != null) PlayerUtils.playerViewModel.currentMusic?.artist else "")
            .setContentIntent(activityPendingIntent)
            .setDeleteIntent(deleteNotificationIntent)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSessionToken)
            ).apply {
                priority = NotificationCompat.PRIORITY_LOW
            }

        notification = notificationBuilder.build()
    }

    abstract fun updateNotification()

    fun getPlayerNotification(): Notification {
        return notification
    }

    fun dismissNotification() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(CHANNEL_ID)
        context.unregisterReceiver(broadcastReceiver)
    }

    companion object {
        const val MUSIC_NOTIFICATION_CHANNEL_ID = "SoulSearchingMusicNotificationChannel"
        const val CHANNEL_ID = 69
    }
}