package com.example.examapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.examapp.Model.Relations.ExamResultWithLectureResults
import com.example.examapp.R
import java.text.SimpleDateFormat
import java.util.*

class ResultAdapter : RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    private var onExamResultClickListener: ((ExamResultWithLectureResults) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_result, parent, false)
        return ViewHolder(view)
    }

    private val diffUtil = object : DiffUtil.ItemCallback<ExamResultWithLectureResults>() {
        override fun areItemsTheSame(
            oldItem: ExamResultWithLectureResults,
            newItem: ExamResultWithLectureResults
        ): Boolean {
            return oldItem.examResult.date == newItem.examResult.date
        }

        override fun areContentsTheSame(
            oldItem: ExamResultWithLectureResults,
            newItem: ExamResultWithLectureResults
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)
    var list: List<ExamResultWithLectureResults>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    fun setOnExamResultClickListener(listener: (ExamResultWithLectureResults) -> Unit) {
        onExamResultClickListener = listener
    }

    override fun onBindViewHolder(holder: ResultAdapter.ViewHolder, position: Int) {
        val currentItem = list.get(position)
        holder.apply {
            examName.setText(currentItem.examResult.examName)
            date.setText(SimpleDateFormat("MM/dd/yyyy").format(Date(currentItem.examResult.date)))
            questions.setText("${currentItem.lectureResults.map { it.question }.sum()} Soru")
            total.setText("${currentItem.examResult.total} Net")
            root.setOnClickListener {
                onExamResultClickListener?.invoke(currentItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var root: ConstraintLayout
        var examName: TextView
        var date: TextView
        var questions: TextView
        var total: TextView

        init {
            root = view.findViewById(R.id.row_result_root)
            examName = view.findViewById(R.id.row_result_name)
            date = view.findViewById(R.id.row_result_date)
            questions = view.findViewById(R.id.row_result_questions)
            total = view.findViewById(R.id.row_result_total)
        }
    }

}