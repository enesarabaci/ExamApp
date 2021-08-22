package com.example.examapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examapp.Model.Relations.ExamWithLectures
import com.example.examapp.Repo.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamViewModel @Inject constructor(
    private val repo: RepositoryInterface
) : ViewModel() {

    private val _exams = MutableLiveData<List<ExamWithLectures>>()
    val exams: LiveData<List<ExamWithLectures>> = _exams

    fun getExams() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repo.getAllExamWithLectures()
            _exams.postValue(data)
        }
    }

}