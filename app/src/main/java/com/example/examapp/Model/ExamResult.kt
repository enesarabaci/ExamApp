package com.example.examapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "examResultTable")
data class ExamResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val examName: String,
    val elimination: Float = 0.25f,
    val date: Long
)