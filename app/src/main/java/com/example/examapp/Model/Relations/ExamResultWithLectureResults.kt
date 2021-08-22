package com.example.examapp.Model.Relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.examapp.Model.ExamResult
import com.example.examapp.Model.LectureResult

data class ExamResultWithLectureResults(
    @Embedded val examResult: ExamResult,
    @Relation(
        parentColumn = "date",
        entityColumn = "examResultDate"
    )
    val lectureResults: List<LectureResult>
)