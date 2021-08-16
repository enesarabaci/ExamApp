package com.example.examapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lectureResultTable")
data class LectureResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val examResultId: Int,
    val name: String,
    val question: Int,
    val trues: Int,
    val falses: Int
)