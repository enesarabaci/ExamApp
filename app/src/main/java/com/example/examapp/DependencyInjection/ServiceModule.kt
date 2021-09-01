package com.example.examapp.DependencyInjection

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.examapp.R
import com.example.examapp.Service.ExamService
import com.example.examapp.Util.Util.ACTION_CANCEL_EXAM
import com.example.examapp.Util.Util.ACTION_SHOW_EXAM_FRAGMENT
import com.example.examapp.Util.Util.NOTIFICATION_CHANNEL_ID
import com.example.examapp.View.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Qualifier

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @FirstImplementor
    @Provides
    @ServiceScoped
    fun createPendingIntent(@ApplicationContext context: Context): PendingIntent =
        PendingIntent.getActivity(context, 0,
            Intent(context, MainActivity::class.java).also {
                it.action = ACTION_SHOW_EXAM_FRAGMENT },
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    @SecondImplementor
    @Provides
    @ServiceScoped
    fun createCancelPendingIntent(@ApplicationContext context: Context): PendingIntent =
        PendingIntent.getService(context, 1,
            Intent(context, ExamService::class.java).also {
                it.action = ACTION_CANCEL_EXAM },
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    @FirstImplementor
    @Provides
    @ServiceScoped
    fun createNotification(
        @ApplicationContext context: Context,
        @FirstImplementor pendingIntent: PendingIntent,
        @SecondImplementor cancelPendingIntent: PendingIntent
    ): NotificationCompat.Builder =
        NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .addAction(R.drawable.ic_baseline_close_24, "Sınavı iptal et", cancelPendingIntent)
            .setSmallIcon(R.drawable.start_exam)
            .setContentIntent(pendingIntent)

    @SecondImplementor
    @Provides
    @ServiceScoped
    fun createResultNotification(
        @ApplicationContext context: Context,
        @FirstImplementor pendingIntent: PendingIntent
    ): NotificationCompat.Builder =
        NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.start_exam)
            .setContentText("Sınavınız tamamlandı. Sonuçları girmek için dokunun.")
            .setContentIntent(pendingIntent)

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FirstImplementor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SecondImplementor