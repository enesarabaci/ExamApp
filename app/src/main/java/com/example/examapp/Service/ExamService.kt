package com.example.examapp.Service

import android.app.NotificationManager
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.examapp.DependencyInjection.FirstImplementor
import com.example.examapp.DependencyInjection.SecondImplementor
import com.example.examapp.Model.Exam
import com.example.examapp.Util.Util
import com.example.examapp.Util.Util.ACTION_CANCEL_EXAM
import com.example.examapp.Util.Util.ACTION_FINISH_EXAM
import com.example.examapp.Util.Util.ACTION_START_EXAM
import com.example.examapp.Util.Util.NOTIFICATION_ID
import com.example.examapp.Util.Util.RESULT_NOTIFICATION_ID
import com.example.examapp.Util.Util.makeTimeString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ExamService : LifecycleService() {

    private var examTime = 0L

    @Inject
    @FirstImplementor
    lateinit var notificationCompatBuilder: NotificationCompat.Builder

    @Inject
    @SecondImplementor
    lateinit var resultNotificationCompatBuilder: NotificationCompat.Builder

    companion object {
        var exam: Exam? = null
        var status = MutableLiveData<Util.SERVICE_STATUS>(Util.SERVICE_STATUS.SERVICE_DOESNT_WORKING)
        var timeInMillis = MutableLiveData<Long>()
    }

    private fun startTimer() {
        val time = examTime
        val startedTime = System.currentTimeMillis()
        var elapsedTime = 0L
        timeInMillis.postValue(examTime-1000)
        CoroutineScope(Dispatchers.Main).launch {
            while (status.value != null && status.value == Util.SERVICE_STATUS.SERVICE_CONTINUES) {
                delay(1000)
                if (examTime <= 0L && status.value == Util.SERVICE_STATUS.SERVICE_CONTINUES) {
                    finishExam()
                    break
                }
                if (status.value != Util.SERVICE_STATUS.SERVICE_DOESNT_WORKING) {
                    elapsedTime = System.currentTimeMillis() - startedTime
                    examTime = time - elapsedTime
                    timeInMillis.postValue(examTime)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            exam = it.getParcelableExtra("exam")
            examTime = exam?.duration ?: 0L
            when (it.action) {
                ACTION_START_EXAM -> { startForegroundService() }
                ACTION_FINISH_EXAM -> { finishExam() }
                ACTION_CANCEL_EXAM -> { cancelExam() }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        val notification = notificationCompatBuilder.setContentTitle(exam?.examName).setContentText(makeTimeString(examTime))
        startForeground(NOTIFICATION_ID, notification.build())

        status.value = Util.SERVICE_STATUS.SERVICE_CONTINUES
        startTimer()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        timeInMillis.observe(this) {
            notificationManager.notify(NOTIFICATION_ID, notification.setContentText(makeTimeString(it)).build())
        }
    }

    private fun showResultNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notification = resultNotificationCompatBuilder.setContentTitle(exam?.examName ?: "S??nav Tamamland??").build()

        notificationManager.notify(RESULT_NOTIFICATION_ID, notification)
    }

    private fun finishExam() {
        showResultNotification()
        status.value = Util.SERVICE_STATUS.SERVICE_FINISHED
        examTime = 0L
        stopForeground(true)
        stopSelf()
        onDestroy()
    }

    private fun cancelExam() {
        status.value = Util.SERVICE_STATUS.SERVICE_DOESNT_WORKING
        examTime = 0L
        stopForeground(true)
        stopSelf()
        onDestroy()
    }

}