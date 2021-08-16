package com.example.examapp.Repo

import com.example.examapp.Model.Relations.ExamWithLectures
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {

    fun getAllExams() : Flow<List<ExamWithLectures>>

}