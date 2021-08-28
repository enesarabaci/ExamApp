package com.example.examapp.View

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.examapp.Model.Relations.ExamResultWithLectureResults
import com.example.examapp.R
import com.example.examapp.Util.Util.prepare
import com.example.examapp.Util.Util.toTwoDecimal
import com.example.examapp.databinding.FragmentExamResultDetailBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class ExamResultDetailFragment : Fragment(R.layout.fragment_exam_result_detail) {

    private lateinit var binding: FragmentExamResultDetailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentExamResultDetailBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.fragmentExamResultDetailToolbar)
            setupActionBarWithNavController(findNavController())
        }
        binding.fragmentExamResultDetailToolbar.setTitle("")

        val result = arguments?.get("examResult") as ExamResultWithLectureResults?
        result?.let { r -> setViews(r).also {
            binding.fragmentExamResultDetailToolbar.setTitle(r.examResult.examName)
        } }
    }

    private fun setViews(result: ExamResultWithLectureResults) {
        binding.apply {
            var trues = 0
            var falses = 0
            val values = ArrayList<PieEntry>()
            result.lectureResults.forEach {
                val row = layoutInflater.inflate(R.layout.row_result_table, null)
                row.findViewById<TextView>(R.id.row_table_lecture).setText("(${it.question}) ${it.name}")
                row.findViewById<TextView>(R.id.row_table_trues).setText("${it.trues}")
                row.findViewById<TextView>(R.id.row_table_falses).setText("${it.falses}")
                row.findViewById<TextView>(R.id.row_table_total).setText(it.total.toTwoDecimal())
                trues += it.trues
                falses += it.falses
                val totalFloat = it.total.toTwoDecimal().replace(",", ".").toFloat()
                values.add(PieEntry(totalFloat, it.name))
                binding.fragmentExamResultDetailTable.addView(row)
            }
            val row = layoutInflater.inflate(R.layout.row_result_table, null)
            row.findViewById<TextView>(R.id.row_table_lecture).setText("Toplam")
            row.findViewById<TextView>(R.id.row_table_trues).setText("${trues}")
            row.findViewById<TextView>(R.id.row_table_falses).setText("${falses}")
            row.findViewById<TextView>(R.id.row_table_total).setText(result.examResult.total.toTwoDecimal())

            val dataSet = PieDataSet(values, "Net Dağılımı")
            /*
            dataSet.apply {
                sliceSpace = 3f
                selectionShift = 5f
                setColors(*ColorTemplate.JOYFUL_COLORS)
            }
            val data = PieData(dataSet)
            data.apply {
                setValueTextSize(10f)
                setValueTextColor(Color.BLACK)
            }
            */

            binding.apply {
                fragmentExamResultDetailTable.addView(row)
                fragmentExamResultDetailTable.requestLayout()
                fragmentExamResultDetailPb.isVisible = false
                fragmentExamResultDetailPc.prepare(dataSet)
                /*
                fragmentExamResultDetailPc.apply {
                    setUsePercentValues(true)
                    description.isEnabled = false
                    setExtraOffsets(5f,5f,5f,5f)
                    dragDecelerationFrictionCoef = 0.95f
                    isDrawHoleEnabled = true
                    setHoleColor(Color.WHITE)
                    transparentCircleRadius = 60f
                    animateY(1000, Easing.EaseInOutCubic)
                    setData(data)
                }
                */
            }
        }
    }

}