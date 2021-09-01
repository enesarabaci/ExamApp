package com.example.examapp.Repo

import com.example.examapp.Model.Exam
import com.example.examapp.Model.ExamResult
import com.example.examapp.Model.Lecture
import com.example.examapp.Model.LectureResult
import com.example.examapp.Model.Relations.ExamResultWithLectureResults
import com.example.examapp.Model.Relations.ExamWithLectures
import com.example.examapp.Room.Dao
import com.example.examapp.Util.Util
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val dao: Dao
) : RepositoryInterface {

    override fun getAllExams(): Flow<List<ExamWithLectures>> = dao.getAllExams()

    override suspend fun insertExamWithLectures(exam: Exam, lectures: List<Lecture>) {
        dao.insertExamWithLectures(exam, lectures)
    }

    override suspend fun getExam(examName: String): List<Exam> = dao.getExam(examName)

    override suspend fun getAllExamWithLectures(): List<ExamWithLectures> = dao.getAllExamWithLectures()

    override suspend fun insertExamResultWithLectureResults(examResult: ExamResult, lectureResults: List<LectureResult>) {
        dao.insertExamResultWithLectureResults(examResult, lectureResults)
    }

    override fun getExamResults(sortType: Util.SORT_TYPES, examName: String?): Flow<List<ExamResultWithLectureResults>> {
        return dao.getExamResults(sortType, examName)
    }

    override suspend fun getExamResultWithLectures(): List<ExamResultWithLectureResults> {
        return dao.getExamResultWithLectures()
    }

    override fun getExamResultsWithName(examName: String): Flow<List<ExamResultWithLectureResults>> {
        return dao.getExamResultsWithName(examName)
    }

}