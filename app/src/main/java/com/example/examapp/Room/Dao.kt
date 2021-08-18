package com.example.examapp.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.examapp.Model.*
import com.example.examapp.Model.Relations.ExamResultWithLectureResults
import com.example.examapp.Model.Relations.ExamWithLectures
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    suspend fun insertExamWithLectures(exam: Exam, lectures: List<Lecture>) {
        insertExam(exam)
        lectures.forEach {
            insertLecture(it)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExam(exam: Exam)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLecture(lecture: Lecture)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamResult(examResult: ExamResult) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLectureResults(lectureResults: List<LectureResult>)

    @Query("SELECT * FROM examTable WHERE examName = :examName")
    fun getExam(examName: String): Flow<List<ExamWithLectures>>

    @Query("SELECT * FROM examTable")
    fun getAllExams(): Flow<List<ExamWithLectures>>

    @Query("SELECT * FROM examResultTable ORDER BY date ASC")
    fun getExamResults(): Flow<List<ExamResultWithLectureResults>>

    @Query("SELECT * FROM examResultTable WHERE examName = :examName ORDER BY date ASC")
    fun getExamResultsWithName(examName: String): Flow<List<ExamResultWithLectureResults>>

}