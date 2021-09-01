package com.example.examapp.View

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.examapp.R
import com.example.examapp.Util.Util.toastBuilder
import java.lang.Exception

class CreateLectureDialogFragment : DialogFragment() {

    private var createListener: ((lectureName: String, lectureQuestions: Int) -> Unit)? = null
    fun setCreateListener(listener: (lectureName: String, lectureQuestions: Int) -> Unit) {
        createListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_create_lecture)
        val lectureName = dialog.findViewById<EditText>(R.id.create_lecture_name)
        val lectureQuestions = dialog.findViewById<EditText>(R.id.create_lecture_questions)
        val saveLecture = dialog.findViewById<Button>(R.id.create_lecture_save)
        saveLecture.setOnClickListener {
            if (lectureName.text.toString().isNotEmpty()) {
                try {
                    val number = lectureQuestions.text.toString().toInt()
                    if (number < 0) {
                        toastBuilder(requireContext(), "Soru sayısı 0' dan büyük olmalı")
                    }else {
                        createListener?.invoke(lectureName.text.toString(), number)
                        dismiss()
                    }
                }catch (e: Exception) {
                    toastBuilder(requireContext(), "Lütfen soru sayısını nümerik bir değer giriniz")
                }
            }else {
                toastBuilder(requireContext(), "Lütfen boş alanları doldur")
            }
        }
        dialog.create()
        return dialog
    }

}