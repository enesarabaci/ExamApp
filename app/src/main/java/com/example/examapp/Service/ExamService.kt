package com.example.examapp.Service

import android.app.NotificationManager
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.examapp.Model.Exam
import com.example.examapp.Model.Relations.ExamWithLectures
import com.example.examapp.Util.Util
import com.example.examapp.Util.Util.ACTION_FINISH_EXAM
import com.example.examapp.Util.Util.ACTION_START_EXAM
import com.example.examapp.Util.Util.NOTIFICATION_ID
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
    lateinit var notificationCompatBuilder: NotificationCompat.Builder

    companion object {
        var exam: Exam? = null
        var status = MutableLiveData<Util.SERVICE_STATUS>(Util.SERVICE_STATUS.SERVICE_DOESNT_WORKING)
        var timeInMillis = MutableLiveData<Long>()
    }

    private fun startTimer() {
        CoroutineScope(Dispatchers.Main).launch {
            while (status.value != null && status.value == Util.SERVICE_STATUS.SERVICE_CONTINUES) {
                delay(1000)
                if (examTime <= 0L && status.value == Util.SERVICE_STATUS.SERVICE_CONTINUES) {
                    finishExam()
                    break
                }
                examTime -= 1000L
                timeInMillis.postValue(examTime)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            exam = it.getParcelableExtra<Exam>("exam") as Exam
            examTime = exam?.duration ?: 0L
            when (it.action) {
                ACTION_START_EXAM -> { startForegroundService() }
                ACTION_FINISH_EXAM -> { finishExam() }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        val notification = notificationCompatBuilder.setContentTitle(exam?.examName).setContentText(makeTimeString(examTime?:0))
        startForeground(NOTIFICATION_ID, notification.build())

        status.value = Util.SERVICE_STATUS.SERVICE_CONTINUES
        startTimer()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        timeInMillis.observe(this) {
            notificationManager.notify(NOTIFICATION_ID, notification.setContentText(makeTimeString(it)).build())
        }
    }

    private fun finishExam() {
        status.value = Util.SERVICE_STATUS.SERVICE_FINISHED
        examTime = 0L
        stopForeground(true)
        stopSelf()
        onDestroy()
    }

}