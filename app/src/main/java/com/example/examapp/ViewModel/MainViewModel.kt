package com.example.examapp.ViewModel

import androidx.lifecycle.*
import com.example.examapp.Model.ExamResult
import com.example.examapp.Model.Relations.ExamResultWithLectureResults
import com.example.examapp.Repo.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: RepositoryInterface
) : ViewModel() {

    private val _data = MutableLiveData<List<ExamResultWithLectureResults>>()
    val data: LiveData<List<ExamResultWithLectureResults>> = _data

    fun getExamResults() {
        viewModelScope.launch(Dispatchers.IO) {
            _data.postValue(repo.getExamResultWithLectures())
        }
    }

}