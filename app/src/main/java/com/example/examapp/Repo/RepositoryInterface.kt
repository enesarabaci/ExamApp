package com.example.examapp.Repo

import com.example.examapp.Model.Exam
import com.example.examapp.Model.ExamResult
import com.example.examapp.Model.Lecture
import com.example.examapp.Model.LectureResult
import com.example.examapp.Model.Relations.ExamWithLectures
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {

    fun getAllExams() : Flow<List<ExamWithLectures>>

    suspend fun insertExamWithLectures(exam: Exam, lectures: List<Lecture>)

    suspend fun getExam(examName: String): List<Exam>

    suspend fun getAllExamWithLectures(): List<ExamWithLectures>

    suspend fun insertExamResultWithLectureResults(examResult: ExamResult, lectureResults: List<LectureResult>)

}