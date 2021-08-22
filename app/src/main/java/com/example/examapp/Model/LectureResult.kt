package com.example.examapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lectureResultTable")
data class LectureResult(
    val examResultDate: Long,
    val name: String,
    val question: Int,
    val trues: Int,
    val falses: Int,
    @PrimaryKey(autoGenerate = false)
    val id: String = "$name$examResultDate"
)