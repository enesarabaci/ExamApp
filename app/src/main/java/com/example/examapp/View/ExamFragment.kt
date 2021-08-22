package com.example.examapp.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.examapp.Model.Relations.ExamWithLectures
import com.example.examapp.R
import com.example.examapp.Service.ExamService
import com.example.examapp.Util.Util
import com.example.examapp.Util.Util.ACTION_FINISH_EXAM
import com.example.examapp.Util.Util.ACTION_START_EXAM
import com.example.examapp.Util.Util.alertDialogBuilder
import com.example.examapp.Util.Util.makeQuestionsText
import com.example.examapp.Util.Util.makeTimeString
import com.example.examapp.Util.Util.snackbarBuilder
import com.example.examapp.ViewModel.ExamViewModel
import com.example.examapp.databinding.FragmentExamBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamFragment : Fragment(R.layout.fragment_exam) {

    private lateinit var binding: FragmentExamBinding
    private val viewModel: ExamViewModel by viewModels()
    private var examList = listOf<ExamWithLectures>()
    private var currentExamIndex = 0

    private var serviceStatus = Util.SERVICE_STATUS.SERVICE_DOESNT_WORKING

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentExamBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        viewModel.getExams()
        collectData()
        binding.apply {
            fragmentExamLeft.setOnClickListener { changeCurrentExam(false) }
            fragmentExamRight.setOnClickListener { changeCurrentExam(true) }
            fragmentExamStartButton.setOnClickListener {
                sendCommendToService(ACTION_START_EXAM)
            }
            fragmentExamFinishButton.setOnClickListener {
                sendCommendToService(ACTION_FINISH_EXAM)
            }
        }
    }

    private fun collectData() {
        viewModel.exams.observe(viewLifecycleOwner) {
            examList = it
            binding.fragmentExamPb.isVisible = false
            if (examList.isNotEmpty()) {
                updateViews()
                binding.fragmentExamLayout.isVisible = true
                binding.fragmentExamEmptyLayout.isVisible = false
            }else {
                binding.fragmentExamEmptyLayout.isVisible = true
                binding.fragmentExamLayout.isVisible = false
            }
        }
        ExamService.timeInMillis.observe(viewLifecycleOwner) {
            binding.fragmentExamTime.setText(makeTimeString(it))
        }
        ExamService.status.observe(viewLifecycleOwner) {
            serviceStatus = it
            updateViews()
        }
    }

    private fun updateViews() {
        when (serviceStatus) {
            Util.SERVICE_STATUS.SERVICE_CONTINUES -> {
                val exam = ExamService.exam
                exam?.let {
                    if (examList.isNotEmpty()) {
                        val nameList = examList.map { it.exam.examName }
                        val index = nameList.indexOf(it.examName)
                        currentExamIndex = index
                        binding.apply {
                            fragmentExamLeft.isVisible = false
                            fragmentExamRight.isVisible = false
                            fragmentExamName.setText(examList.get(currentExamIndex).exam.examName)
                            fragmentExamLectureQuestions.setText(makeQuestionsText(examList.get(currentExamIndex)))
                            fragmentExamStartButton.isVisible = false
                            fragmentExamFinishButton.isVisible = true
                        }
                    }
                }
            }
            Util.SERVICE_STATUS.SERVICE_FINISHED -> {
                val exam = ExamService.exam
                exam?.let {
                    if (examList.isNotEmpty()) {
                        val nameList = examList.map { it.exam.examName }
                        val index = nameList.indexOf(it.examName)
                        currentExamIndex = index
                        binding.apply {
                            fragmentExamLeft.isVisible = false
                            fragmentExamRight.isVisible = false
                            fragmentExamName.setText(examList.get(currentExamIndex).exam.examName)
                            fragmentExamLectureQuestions.setText(makeQuestionsText(examList.get(currentExamIndex)))
                            fragmentExamStartButton.isVisible = false
                            fragmentExamFinishButton.isVisible = false
                        }
                        ExamService.status.value = Util.SERVICE_STATUS.SERVICE_DOESNT_WORKING
                        binding.fragmentExamTime.setText(makeTimeString(examList.get(currentExamIndex).exam.duration))

                        alertDialogBuilder(requireContext(), "", "Sonuçları kaydetmek ister misiniz?", { /* Yes */
                            findNavController().navigate(ExamFragmentDirections.actionExamFragmentToExamResultFragment(examList.get(currentExamIndex)))
                        }, { /* No */
                            binding.fragmentExamTime.setText(makeTimeString(examList.get(currentExamIndex).exam.duration))
                        }, { /* Dismiss */
                            binding.fragmentExamTime.setText(makeTimeString(examList.get(currentExamIndex).exam.duration))
                        })
                    }
                }
            }
            Util.SERVICE_STATUS.SERVICE_DOESNT_WORKING -> {
                binding.apply {
                    if (examList.isNotEmpty()) {
                        val currentExam = examList.get(currentExamIndex)
                        fragmentExamLeft.isVisible = true
                        fragmentExamRight.isVisible = true
                        fragmentExamName.setText(currentExam.exam.examName)
                        fragmentExamLectureQuestions.setText(makeQuestionsText(currentExam))
                        fragmentExamTime.setText(makeTimeString(currentExam.exam.duration))
                        fragmentExamStartButton.isVisible = true
                        fragmentExamFinishButton.isVisible = false
                    }
                }
            }
        }
    }

    private fun sendCommendToService(serviceAction: String) {
        val intent = Intent(requireContext(), ExamService::class.java)
        intent.apply {
            action = serviceAction
            putExtra("exam", examList.get(currentExamIndex).exam)
        }
        requireContext().startService(intent)
    }

    private fun changeCurrentExam(right: Boolean) {
        val size = examList.size - 1
        if (right) {
            if (currentExamIndex != size) {
                currentExamIndex++
            }else {
                currentExamIndex = 0
            }
        }else {
            if (currentExamIndex != 0) {
                currentExamIndex--
            }else {
                currentExamIndex = size
            }
        }
        updateViews()
    }

}