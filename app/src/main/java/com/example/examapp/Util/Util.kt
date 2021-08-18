package com.example.examapp.Util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

object Util {

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

    fun calculateExamResult(trues: Int, falses: Int, elimination: Float) : Float =
        (trues - (falses * elimination))
    
    fun alertDialogBuilder(context: Context, title: String, message: String, yes: (() -> Unit)) {
        val builder = AlertDialog.Builder(context)

        builder
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Evet", DialogInterface.OnClickListener { dialogInterface, i ->
                yes.invoke()
            })
            .setNegativeButton("Hayır", null)
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
        return hashMapOf("hours" to hours.toInt(), "minutes" to minutes.toInt())
    }

    fun makeTimeString(ms: Long): String {
        val map = makeTime(ms)
        val hours = map.get("hours")
        val minutes = map.get("minutes")
        if (hours != null && minutes != null) {
            return "${if (hours < 10) "0" else "" }$hours:${if (minutes < 10)"0" else ""}$minutes"
        }
        return "00:00"
    }

    val ELIMINATIONS = arrayOf<Float>(
        0f,
        (1/1f),
        (1/2f),
        (1/3f),
        (1/4f)
    )

}