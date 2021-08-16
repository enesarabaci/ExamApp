package com.example.examapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.examapp.Model.Relations.ExamWithLectures
import com.example.examapp.Repo.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ExamsViewModel @Inject constructor(
    private val repo: RepositoryInterface
) : ViewModel() {

    private val _data = repo.getAllExams()
    val data: Flow<List<ExamWithLectures>> = _data

}