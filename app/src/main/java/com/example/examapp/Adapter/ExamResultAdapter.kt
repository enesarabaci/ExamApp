package com.example.examapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.examapp.Model.Lecture
import com.example.examapp.R
import com.example.examapp.Util.Util.makeTotal
import com.example.examapp.Util.Util.toTwoDecimal
import java.lang.Exception

class ExamResultAdapter : RecyclerView.Adapter<ExamResultAdapter.ViewHolder>() {

    private var onTruesTextListener: ((Int, String) -> Unit)? = null
    private var onFalsesTextListener: ((Int, String) -> Unit)? = null
    private var list = listOf<Lecture>()
    var elimination = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExamResultAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_exam_result, parent, false)
        return ViewHolder(view)
    }

    fun setOnTruesTextListener(listener: (Int, String) -> Unit) {
        onTruesTextListener = listener
    }
    fun setOnFalsesTextListener(listener: (Int, String) -> Unit) {
        onFalsesTextListener = listener
    }

    override fun onBindViewHolder(holder: ExamResultAdapter.ViewHolder, position: Int) {
        val currentLecture = list.get(position)
        holder.apply {
            lectureName.setText("(${currentLecture.question}) ${currentLecture.name}")
            trues.doOnTextChanged { text, start, before, count ->
                onTruesTextListener?.invoke(position, text.toString())
                try {
                    val truesInt = text.toString().toInt()
                    val falsesInt = falses.text.toString().toInt()
                    val value = makeTotal(truesInt, falsesInt, elimination)
                    total.setText(value.toTwoDecimal())
                }catch (e: Exception) {
                    total.setText("")
                }
            }
            falses.doOnTextChanged { text, start, before, count ->
                onFalsesTextListener?.invoke(position, text.toString())
                try {
                    val falsesInt = text.toString().toInt()
                    val truesInt = trues.text.toString().toInt()
                    val value = makeTotal(truesInt, falsesInt, elimination)
                    total.setText(value.toTwoDecimal())
                }catch (e: Exception) {
                    total.setText("")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var lectureName: TextView
        var trues: EditText
        var falses: EditText
        var total: TextView
        init {
            lectureName = view.findViewById(R.id.row_exam_result_lecture_name)
            trues = view.findViewById(R.id.row_exam_result_trues)
            falses = view.findViewById(R.id.row_exam_result_falses)
            total = view.findViewById(R.id.row_exam_result_total)
        }
    }

    fun updateList(newList: List<Lecture>) {
        list = newList
        notifyDataSetChanged()
    }

}