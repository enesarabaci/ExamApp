package com.example.examapp.View

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.examapp.Model.Relations.ExamResultWithLectureResults
import com.example.examapp.R
import com.example.examapp.Util.Util.prepare
import com.example.examapp.Util.Util.toTwoDecimal
import com.example.examapp.ViewModel.MainViewModel
import com.example.examapp.databinding.FragmentMainBinding
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMainBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        viewModel.getExamResults()
        collectData()
    }

    private fun collectData() {
        viewModel.data.observe(viewLifecycleOwner) {
            updateLatestExams(it)
        }
    }

    private fun updateLatestExams(list: List<ExamResultWithLectureResults>) {
        if (list.isEmpty()) {
            binding.fragmentMainExamWarning.isVisible = true
            return
        }else {
            binding.fragmentMainExamsLayout.isVisible = true
            var trues = 0
            var falses = 0
            list.map { it.lectureResults }.forEach {
                it.forEach {
                    trues += it.trues
                    falses += it.falses
                }
            }
            val values = mutableListOf<PieEntry>().apply {
                add(PieEntry(trues.toFloat(), "Doğru Cevaplar"))
                add(PieEntry(falses.toFloat(), "Yanlış Cevaplar"))
            }
            val dataSet = PieDataSet(values, "Doğru-Yanlış Dağılımı")
            binding.apply {
                fragmentMainExamsLayout.isVisible = true
                fragmentMainExamName1.setText(list.get(0).examResult.examName)
                fragmentMainExamTotal1.setText("${list.get(0).examResult.total.toTwoDecimal()} Net")
                if (list.size >= 2) {
                    fragmentMainExamName2.setText(list.get(1).examResult.examName)
                    fragmentMainExamTotal2.setText("${list.get(1).examResult.total.toTwoDecimal()} Net")
                }
                if (list.size >= 3) {
                    fragmentMainExamName3.setText(list.get(2).examResult.examName)
                    fragmentMainExamTotal3.setText("${list.get(2).examResult.total.toTwoDecimal()} Net")
                }
                fragmentMainExamCountTitle.setText("Şu ana kadar toplam ${list.size} sınava girdin.")
                fragmentMainExamPc.prepare(
                    dataSet,
                    ContextCompat.getColor(requireContext(), R.color.teal_500),
                    ContextCompat.getColor(requireContext(), R.color.light_red)
                )
            }
        }
    }

}