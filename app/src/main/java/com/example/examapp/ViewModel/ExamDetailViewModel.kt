package com.example.examapp.ViewModel

import androidx.lifecycle.*
import com.example.examapp.Model.Relations.ExamResultWithLectureResults
import com.example.examapp.Repo.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExamDetailViewModel @Inject constructor(
    private val repo: RepositoryInterface
) : ViewModel() {

    val examName = MutableLiveData<String>()

    val examResults: LiveData<List<ExamResultWithLectureResults>> = examName.switchMap {
        repo.getExamResultsWithName(it).asLiveData()
    }

}