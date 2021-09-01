package com.example.examapp.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.examapp.Model.Relations.ExamWithLectures
import com.example.examapp.R
import com.example.examapp.Util.Util.makeTimeString

class ExamsAdapter(private val context: Context) : RecyclerView.Adapter<ExamsAdapter.ViewHolder>() {

    private var onExamClickListener: ((ExamWithLectures) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_exams, parent, false)
        return ViewHolder(view)
    }

    private val diffUtil = object: DiffUtil.ItemCallback<ExamWithLectures>() {
        override fun areItemsTheSame(
            oldItem: ExamWithLectures,
            newItem: ExamWithLectures
        ): Boolean {
            return oldItem.exam.examName == newItem.exam.examName
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ExamWithLectures,
            newItem: ExamWithLectures
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)
    var list: List<ExamWithLectures>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    fun setOnExamClickListener(listener: (ExamWithLectures) -> Unit) {
        onExamClickListener = listener
    }

    override fun onBindViewHolder(holder: ExamsAdapter.ViewHolder, position: Int) {
        val currentExam = list.get(position)
        holder.apply {
            examName.setText(currentExam.exam.examName)
            examTime.setText(makeTimeString(currentExam.exam.duration))
            val eliminations = context.resources.getStringArray(R.array.eliminations2)
            examElimination.setText(eliminations.get(currentExam.exam.elimination))
            var questions = 0
            currentExam.lectures.forEach {
                questions += it.question
            }
            examQuestions.setText("$questions soru")
            val lectures = StringBuilder()
            currentExam.lectures.forEach {
                if (lectures.isNotEmpty()) { lectures.append("\n") }
                lectures.append("${it.name} (${it.question})")
            }
            examLectures.setText(lectures.toString())
            examInfo.setOnClickListener {
                onExamClickListener?.invoke(currentExam)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var examInfo: ImageButton
        var examName: TextView
        var examTime: TextView
        var examElimination: TextView
        var examQuestions: TextView
        var examLectures: TextView
        init {
            examInfo = view.findViewById(R.id.row_exam_info)
            examName = view.findViewById(R.id.row_exam_name)
            examTime = view.findViewById(R.id.row_exam_time)
            examElimination = view.findViewById(R.id.row_exam_elimination)
            examQuestions = view.findViewById(R.id.row_exam_questions)
            examLectures = view.findViewById(R.id.row_exam_lectures)
        }
    }

}