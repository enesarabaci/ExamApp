package com.example.examapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "examResultTable")
data class ExamResult(
    val examName: String,
    val elimination: Int,
    val total: Double,
    @PrimaryKey(autoGenerate = false)
    val date: Long
)