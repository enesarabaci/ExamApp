package com.example.examapp.Model.Relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.examapp.Model.Exam
import com.example.examapp.Model.Lecture

class ExamWithLectures(
    @Embedded val exam: Exam,
    @Relation(
        entityColumn = "examName",
        parentColumn = "examName"
    )
    val lectures: List<Lecture>
)