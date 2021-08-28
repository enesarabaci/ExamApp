package com.example.examapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.examapp.Repo.RepositoryInterface
import com.example.examapp.Util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val repo: RepositoryInterface
) : ViewModel() {

    var sortType = MutableStateFlow<Util.SORT_TYPES>(Util.SORT_TYPES.SORT_BY_DATE)
    var examName = MutableStateFlow<String?>(null)

    @ExperimentalCoroutinesApi
    private val resultsFlow = combine(sortType, examName) { sort, exam ->
        Pair(sort, exam)
    }.flatMapLatest { (sortType, examName) ->
        repo.getExamResults(sortType, examName)
    }

    fun updateSortType(type: Util.SORT_TYPES) {
        viewModelScope.launch {
            sortType.emit(type)
        }
    }
    fun updateExamName(name: String?) {
        viewModelScope.launch {
            examName.emit(name)
        }
    }

    @ExperimentalCoroutinesApi
    val results = resultsFlow.asLiveData()

}