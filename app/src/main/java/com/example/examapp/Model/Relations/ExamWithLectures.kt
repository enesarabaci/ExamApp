package com.example.examapp.Model.Relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.examapp.Model.Exam
import com.example.examapp.Model.Lecture
import java.io.Serializable

class ExamWithLectures(
    @Embedded val exam: Exam,
    @Relation(
        parentColumn = "examName",
        entityColumn = "examName"
    )
    val lectures: List<Lecture>
) : Serializable