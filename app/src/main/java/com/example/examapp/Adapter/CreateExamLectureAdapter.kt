package com.example.examapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.examapp.Model.Lecture
import com.example.examapp.R

class CreateExamLectureAdapter : RecyclerView.Adapter<CreateExamLectureAdapter.ViewHolder>() {

    private var onItemDeleteListener: ((Lecture) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_create_exam_lecture, parent, false)
        return ViewHolder(view)
    }

    private val diffUtil = object: DiffUtil.ItemCallback<Lecture>() {
        override fun areItemsTheSame(oldItem: Lecture, newItem: Lecture): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Lecture, newItem: Lecture): Boolean {
            return oldItem == newItem
        }
    }
    private val recyclerListDiffer = AsyncListDiffer<Lecture>(this, diffUtil)
    var list: List<Lecture>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    fun setOnItemDeleteListener(listener: (Lecture) -> Unit) {
        onItemDeleteListener = listener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentLecture = list.get(position)
        holder.apply {
            lectureText.setText("${currentLecture.name} (${currentLecture.question})")
            deleteLecture.setOnClickListener {
                onItemDeleteListener?.invoke(currentLecture)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var lectureText: TextView
        var deleteLecture: ImageButton
        init {
            lectureText = view.findViewById(R.id.row_create_exam_lecture_name)
            deleteLecture = view.findViewById(R.id.row_create_exam_lecture_delete)
        }
    }

}