package com.example.examapp.Util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.examapp.Model.Exam
import com.example.examapp.Model.Relations.ExamWithLectures
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.first
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

object Util {

    /*
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userName")
    private val dataStoreKey = stringPreferencesKey("name")

    suspend fun getUserName(context: Context) : String? {
        val preferences = context.dataStore.data.first()
        return preferences.get(dataStoreKey)
    }

    suspend fun writeUserName(context: Context, userName: String) {
        context.dataStore.edit { settings ->
            settings[dataStoreKey] = userName
        }
    }
    */
    
    fun alertDialogBuilder(context: Context, title: String, message: String, yes: (() -> Unit), no: (() -> Unit)? = null, dismiss: (() -> Unit)? = null) {
        val builder = AlertDialog.Builder(context)

        builder
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Evet", DialogInterface.OnClickListener { dialogInterface, i ->
                yes.invoke()
            })
            .setNegativeButton("Hayır", DialogInterface.OnClickListener { dialogInterface, i ->
                no?.invoke()
            })
            .setOnDismissListener {
                dismiss?.invoke()
            }
            .show()
    }

    fun toastBuilder(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun snackbarBuilder(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun makeMilliseconds(hours: Int, minutes: Int): Long {
        var miliseconds = 0L
        miliseconds += TimeUnit.HOURS.toMillis(hours.toLong())
        miliseconds += TimeUnit.MINUTES.toMillis(minutes.toLong())
        return miliseconds
    }

    fun makeTime(ms: Long): Map<String, Int> {
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
        return hashMapOf("hours" to hours.toInt(), "minutes" to minutes.toInt(), "seconds" to seconds.toInt())
    }

    fun makeTimeString(ms: Long): String {
        val map = makeTime(ms)
        val hours = map.get("hours")
        val minutes = map.get("minutes")
        val seconds = map.get("seconds")
        if (hours != null && minutes != null && seconds != null) {
            return "${if (hours < 10) "0" else "" }$hours:${if (minutes < 10)"0" else ""}$minutes:${if (seconds < 10)"0" else ""}$seconds"
        }
        return "00:00:00"
    }

    fun makeQuestionsText(exam: ExamWithLectures): String {
        var questions = 0
        exam.lectures.forEach {
            questions += it.question
        }
        return "$questions soru"
    }

    fun makeTotal(trues: Int, falses: Int, elimination: Int): Double {
        var total = 0.0
        total = trues - (falses/elimination).toDouble()
        total -= (falses%elimination).toDouble()/elimination
        return total
    }
    fun Double.toTwoDecimal(): String = String.format("%.2f", this)

    val ELIMINATIONS = arrayOf<Int>(
        0,
        1,
        2,
        3,
        4
    )

    fun PieChart.prepare(dataSet: PieDataSet, vararg colors: Int = ColorTemplate.JOYFUL_COLORS) {
        dataSet.apply {
            sliceSpace = 3f
            selectionShift = 5f
            setColors(*colors)
        }
        val data = PieData(dataSet)
        data.apply {
            setValueTextSize(10f)
            setValueTextColor(Color.BLACK)
        }
        setUsePercentValues(true)
        description.isEnabled = false
        setExtraOffsets(5f,5f,5f,5f)
        dragDecelerationFrictionCoef = 0.95f
        isDrawHoleEnabled = true
        setHoleColor(Color.WHITE)
        transparentCircleRadius = 60f
        animateY(1000, Easing.EaseInOutCubic)
        setData(data)
    }

    enum class SERVICE_STATUS {
        SERVICE_CONTINUES,
        SERVICE_FINISHED,
        SERVICE_DOESNT_WORKING
    }

    enum class SORT_TYPES {
        SORT_BY_DATE,
        SORT_BY_TOTAL
    }

    const val NOTIFICATION_CHANNEL_ID = "exam_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Exam"
    const val NOTIFICATION_ID = 1

    const val ACTION_START_EXAM = "ACTION_START_EXAM"
    const val ACTION_FINISH_EXAM = "FINISH_EXAM"

    const val ACTION_SHOW_EXAM_FRAGMENT = "ACTION_SHOW_EXAM_FRAGMENT"

}