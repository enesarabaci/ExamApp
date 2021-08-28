package com.example.examapp.ViewModel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examapp.Model.ExamResult
import com.example.examapp.Model.LectureResult
import com.example.examapp.Model.Relations.ExamWithLectures
import com.example.examapp.Repo.RepositoryInterface
import com.example.examapp.Util.Util.makeTotal
import com.example.examapp.Util.Util.snackbarBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamResultViewModel @Inject constructor(
    private val repo: RepositoryInterface
) : ViewModel() {

    val trueResultsMap = hashMapOf<String, Int>()
    val falseResultsMap = hashMapOf<String, Int>()
    var examWithLectures: ExamWithLectures? = null

    fun saveExamResult(view: View): Boolean {
        examWithLectures?.let {
            if (trueResultsMap.size < it.lectures.size || falseResultsMap.size < it.lectures.size) {
                snackbarBuilder(view, "Lütfen bütün alanları nümerik olarak doldurunuz")
                return false
            } else if (trueResultsMap.size == it.lectures.size && falseResultsMap.size == it.lectures.size) {
                val date = System.currentTimeMillis()
                val lectureResults = arrayListOf<LectureResult>()
                it.lectures.forEach { lecture ->
                    val lectureQuestions = lecture.question
                    val resultQuestions = (trueResultsMap.get(lecture.name) ?: 0) + (falseResultsMap.get(lecture.name) ?: 0)
                    if(resultQuestions > lectureQuestions) {
                        snackbarBuilder(view, "${lecture.name} dersi için girilen sonuçlar soru sayısından fazla")
                        return false
                    }
                    lectureResults.add(
                        LectureResult(
                            date,
                            lecture.name,
                            lecture.question,
                            trueResultsMap.get(lecture.name) ?: 0,
                            falseResultsMap.get(lecture.name) ?: 0,
                            makeTotal(
                                trueResultsMap.get(lecture.name) ?: 0,
                                falseResultsMap.get(lecture.name) ?: 0,
                                it.exam.elimination
                            )
                        )
                    )
                }
                val total = lectureResults.map { it.total }.sum()
                val examResult = ExamResult(it.exam.examName, it.exam.elimination,total , date)
                viewModelScope.launch(Dispatchers.IO) {
                    repo.insertExamResultWithLectureResults(examResult, lectureResults.toList())
                }
                return true
            } else {
                return false
            }
        } ?: return false
    }

}