package com.example.examapp.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.examapp.Model.Exam
import com.example.examapp.Model.ExamResult
import com.example.examapp.Model.Lecture
import com.example.examapp.Model.LectureResult

@Database(entities = arrayOf(
    Exam::class,
    Lecture::class,
    ExamResult::class,
    LectureResult::class
), version = 1)
abstract class ExamDatabase : RoomDatabase() {

    abstract fun dao(): Dao

}