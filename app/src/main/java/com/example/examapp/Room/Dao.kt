package com.example.examapp.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.examapp.Model.*
import com.example.examapp.Model.Relations.ExamResultWithLectureResults
import com.example.examapp.Model.Relations.ExamWithLectures
import com.example.examapp.Util.Util
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    suspend fun insertExamWithLectures(exam: Exam, lectures: List<Lecture>) {
        insertExam(exam)
        lectures.forEach {
            insertLecture(it)
        }
    }

    suspend fun insertExamResultWithLectureResults(examResult: ExamResult, lectureResults: List<LectureResult>) {
        insertExamResult(examResult)
        lectureResults.forEach {
            insertLectureResult(it)
        }
    }

    fun getExamResults(sortType: Util.SORT_TYPES, examName: String?): Flow<List<ExamResultWithLectureResults>> {
        return when (sortType) {
            Util.SORT_TYPES.SORT_BY_DATE -> examName?.let { getExamResultsSortByDateWithExamName(it) } ?: getExamResultsSortByDate()
            Util.SORT_TYPES.SORT_BY_TOTAL -> examName?.let { getExamResultsSortByTotalWithExamName(it) } ?: getExamResultsSortByTotal()
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExam(exam: Exam)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLecture(lecture: Lecture)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamResult(examResult: ExamResult) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLectureResult(lectureResult: LectureResult)

    @Query("SELECT * FROM examTable WHERE examName = :examName")
    suspend fun getExam(examName: String): List<Exam>

    @Query("SELECT * FROM examTable WHERE examName = :examName")
    fun getExamWithLectures(examName: String): Flow<List<ExamWithLectures>>

    @Query("SELECT * FROM examTable")
    suspend fun getAllExamWithLectures(): List<ExamWithLectures>

    @Query("SELECT * FROM examTable")
    fun getAllExams(): Flow<List<ExamWithLectures>>

    @Query("SELECT * FROM examResultTable WHERE examName = :examName ORDER BY date DESC")
    fun getExamResultsSortByDateWithExamName(examName: String): Flow<List<ExamResultWithLectureResults>>
    @Query("SELECT * FROM examResultTable ORDER BY date DESC")
    fun getExamResultsSortByDate(): Flow<List<ExamResultWithLectureResults>>

    @Query("SELECT * FROM examResultTable WHERE examName = :examName ORDER BY total DESC")
    fun getExamResultsSortByTotalWithExamName(examName: String): Flow<List<ExamResultWithLectureResults>>
    @Query("SELECT * FROM examResultTable ORDER BY total DESC")
    fun getExamResultsSortByTotal(): Flow<List<ExamResultWithLectureResults>>

    @Query("SELECT * FROM examResultTable WHERE examName = :examName ORDER BY date DESC")
    fun getExamResultsWithName(examName: String): Flow<List<ExamResultWithLectureResults>>

    @Query("SELECT * FROM examResultTable ORDER BY date DESC")
    suspend fun getExamResultWithLectures(): List<ExamResultWithLectureResults>

}