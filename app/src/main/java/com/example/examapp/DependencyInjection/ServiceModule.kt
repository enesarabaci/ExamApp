package com.example.examapp.DependencyInjection

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.examapp.R
import com.example.examapp.Util.Util.ACTION_SHOW_EXAM_FRAGMENT
import com.example.examapp.Util.Util.NOTIFICATION_CHANNEL_ID
import com.example.examapp.View.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    @ServiceScoped
    fun createPendingIntent(@ApplicationContext context: Context): PendingIntent =
        PendingIntent.getActivity(context, 0,
            Intent(context, MainActivity::class.java).also {
                it.action = ACTION_SHOW_EXAM_FRAGMENT },
            PendingIntent.FLAG_UPDATE_CURRENT)

    @Provides
    @ServiceScoped
    fun createNotification(@ApplicationContext context: Context, pendingIntent: PendingIntent): NotificationCompat.Builder =
        NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.start_exam)
            .setContentIntent(pendingIntent)




}