package com.example.examapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "examTable")
data class Exam(
    @PrimaryKey(autoGenerate = false)
    val examName: String,
    val duration: Long,
    val elimination: Float
)