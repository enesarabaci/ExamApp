package com.example.examapp.View

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.arraySetOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.examapp.Model.Relations.ExamResultWithLectureResults
import com.example.examapp.Model.Relations.ExamWithLectures
import com.example.examapp.R
import com.example.examapp.Util.Util
import com.example.examapp.Util.Util.prepare
import com.example.examapp.Util.Util.toTwoDecimal
import com.example.examapp.ViewModel.ExamDetailViewModel
import com.example.examapp.databinding.FragmentExamDetailBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamDetailFragment : Fragment(R.layout.fragment_exam_detail) {

    private lateinit var binding: FragmentExamDetailBinding
    private val viewModel: ExamDetailViewModel by viewModels()
    private val lectures = arraySetOf("Genel")
    private var exam: ExamWithLectures? = null
    private var list = listOf<ExamResultWithLectureResults>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentExamDetailBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.fragmentExamDetailToolbar)
            setupActionBarWithNavController(findNavController())
        }

        exam = arguments?.get("exam") as ExamWithLectures?
        exam?.let {
            viewModel.examName.value = it.exam.examName
            binding.apply {
                fragmentExamDetailToolbar.setTitle(it.exam.examName)

                fragmentExamDetailTime.setText(Util.makeTimeString(it.exam.duration))
                val eliminations = requireContext().resources.getStringArray(R.array.eliminations2)
                fragmentExamDetailElimination.setText(eliminations.get(it.exam.elimination))
                val questions = it.lectures.map { it.question }.sum()
                fragmentExamDetailQuestions.setText("$questions soru")
            }
        }
        collectData()
    }

    override fun onResume() {
        buildActv()
        super.onResume()
    }

    private fun buildActv() {
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, lectures.toArray())
        binding.fragmentExamDetailActv.setAdapter(arrayAdapter)
        binding.fragmentExamDetailActv.setOnItemClickListener { adapterView, view, position, l ->
            if (position == lectures.toArray().size-1) {
                setViews()
            }else {
                setViews(lectures.toArray().get(position) as String)
            }
        }
    }

    private fun collectData() {
        viewModel.examResults.observe(viewLifecycleOwner) {
            list = it
            list.map { it.lectureResults }.forEach {
                it.forEach {
                    lectures.add(it.name)
                }
            }
            buildActv()
            setViews()
        }
    }

    private fun setViews(filter: String? = null) {
        var totalFloat = 0f
        val values = ArrayList<Entry>()
        for (i in 1..list.size) {
            totalFloat = filter?.let {
                list.get(i-1).lectureResults.find { it.name == filter }?.total?.toFloat()
            } ?: list.get(i-1).examResult.total.toTwoDecimal().replace(",", ".").toFloat()

            values.add(Entry(i.toFloat(), totalFloat))
        }
        
        val dataSet = LineDataSet(values, filter?.let { "$it Dersi Netleri" } ?: "Genel Netler")
        binding.fragmentExamDetailLc.prepare(dataSet)
    }

}