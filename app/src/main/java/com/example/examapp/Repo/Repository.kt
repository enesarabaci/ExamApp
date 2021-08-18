package com.example.examapp.Repo

import com.example.examapp.Model.Exam
import com.example.examapp.Model.Lecture
import com.example.examapp.Model.Relations.ExamWithLectures
import com.example.examapp.Room.Dao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val dao: Dao
) : RepositoryInterface {

    override fun getAllExams(): Flow<List<ExamWithLectures>> = dao.getAllExams()

    override suspend fun insertExamWithLectures(exam: Exam, lectures: List<Lecture>) {
        dao.insertExamWithLectures(exam, lectures)
    }

}