package com.example.examapp.Util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime

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
    

}