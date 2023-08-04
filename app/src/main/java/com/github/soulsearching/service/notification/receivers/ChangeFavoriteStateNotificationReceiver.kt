package com.github.soulsearching.service.notification.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.soulsearching.classes.player.SoulSearchingMediaPlayerImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangeFavoriteStateNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            val intentForNotification = Intent(SoulSearchingMediaPlayerImpl.BROADCAST_NOTIFICATION)
            intentForNotification.putExtra("FAVORITE_CHANGED", true)
            context.sendBroadcast(intentForNotification)
        }
    }
}