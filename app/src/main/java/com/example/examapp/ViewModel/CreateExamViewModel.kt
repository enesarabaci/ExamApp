package com.example.examapp.ViewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examapp.Model.Exam
import com.example.examapp.Model.Lecture
import com.example.examapp.Repo.RepositoryInterface
import com.example.examapp.Util.Util.makeMilliseconds
import com.example.examapp.Util.Util.makeTimeString
import com.example.examapp.Util.Util.snackbarBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateExamViewModel @Inject constructor(
    private val repo: RepositoryInterface
) : ViewModel() {

    private val _createdLectures = MutableLiveData<ArrayList<Lecture>>(arrayListOf())
    val createdLectures: LiveData<ArrayList<Lecture>> = _createdLectures

    private val _finish = MutableLiveData(false)
    val finish: LiveData<Boolean> = _finish

    var examName = ""
    var elimination = 0
    var selectedTime = 0L

    fun getTimeString(): String = makeTimeString(selectedTime)

    fun addLecture(lectureName: String, lectureQuestions: Int) {
        val lecture = Lecture(lectureName, "", lectureQuestions)
        _createdLectures.value?.apply {
            add(lecture)
            _createdLectures.postValue(this)
        }
    }

    fun deleteLecture(lecture: Lecture) {
        _createdLectures.value?.apply {
            remove(lecture)
            _createdLectures.postValue(this)
        }
    }

    fun saveExam(view: View) {
        _createdLectures.value?.apply {
            val lectures = arrayListOf<Lecture>()
            if (examName.isEmpty()) {
                snackbarBuilder(view, "Lütfen sınav adını giriniz")
                return
            }
            forEach {
                lectures.add(Lecture(it.name, examName, it.question))
            }
            if (lectures.isEmpty()) {
                snackbarBuilder(view, "En az bir ders eklemeniz gerekir")
                return
            }
            if (selectedTime <= 0L) {
                snackbarBuilder(view, "Lütfen bir sınav süresi belirleyin")
                return
            }
            val exam = Exam(examName, selectedTime, elimination)
            viewModelScope.launch(Dispatchers.IO) {
                val list = repo.getExam(examName)
                if (list.isEmpty()) {
                    repo.insertExamWithLectures(exam, lectures)
                    _finish.postValue(true)
                }else {
                    snackbarBuilder(view, "Zaten bu isme sahip bir sınav buluyor")
                }
            }
        } ?: kotlin.run {
            snackbarBuilder(view, "En az bir ders eklemeniz gerekir")
        }
    }

}