package com.example.examapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "lectureTable")
data class Lecture(
    val name: String,
    var examName: String,
    val question: Int,
    @PrimaryKey(autoGenerate = false)
    val id: String = "$name$examName"
) : Serializable