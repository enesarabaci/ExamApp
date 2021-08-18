package com.example.examapp.Repo

import com.example.examapp.Model.Exam
import com.example.examapp.Model.Lecture
import com.example.examapp.Model.Relations.ExamWithLectures
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {

    fun getAllExams() : Flow<List<ExamWithLectures>>

    suspend fun insertExamWithLectures(exam: Exam, lectures: List<Lecture>)

}