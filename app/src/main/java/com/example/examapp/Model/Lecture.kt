package com.example.examapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lectureTable")
data class Lecture(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val examName: String,
    val question: Int
)