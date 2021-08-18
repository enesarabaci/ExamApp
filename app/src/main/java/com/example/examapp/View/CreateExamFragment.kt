package com.example.examapp.View

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examapp.Adapter.CreateExamLectureAdapter
import com.example.examapp.Model.Lecture
import com.example.examapp.R
import com.example.examapp.Util.Util.ELIMINATIONS
import com.example.examapp.Util.Util.alertDialogBuilder
import com.example.examapp.Util.Util.makeMilliseconds
import com.example.examapp.Util.Util.makeTime
import com.example.examapp.Util.Util.makeTimeString
import com.example.examapp.ViewModel.CreateExamViewModel
import com.example.examapp.databinding.FragmentCreateExamBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CreateExamFragment : Fragment(R.layout.fragment_create_exam) {

    private lateinit var binding: FragmentCreateExamBinding
    private val viewModel: CreateExamViewModel by viewModels()
    private val recyclerAdapter = CreateExamLectureAdapter()
    private var selectedTime = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCreateExamBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.fragmentCreateExamToolbar)
            setupActionBarWithNavController(findNavController())
        }
        binding.fragmentCreateExamToolbar.title = "Yeni Sınav Ekle"
        setHasOptionsMenu(true)

        collectData()
        setupRv()
        binding.fragmentCreateExamTime.setOnClickListener { timeClick() }
        binding.fragmentCreateExamAddLecture.setOnClickListener { addLecture() }
        binding.fragmentCreateExamName.doOnTextChanged { text, start, before, count ->
            viewModel.examName = text.toString()
        }
    }

    override fun onResume() {
        buildActv()
        super.onResume()
    }

    private fun collectData() {
        viewModel.createdLectures.observe(viewLifecycleOwner) {
            recyclerAdapter.list = it.toList()
            binding.fragmentCreateExamRv.setHasFixedSize(true)
        }
    }

    private fun setupRv() {
        binding.fragmentCreateExamRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
            setHasFixedSize(true)
        }
        recyclerAdapter.setOnItemDeleteListener { lecture ->
            viewModel.deleteLecture(lecture)
        }
    }

    private fun buildActv() {
        val eliminations = resources.getStringArray(R.array.eliminations)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, eliminations)
        binding.fragmentCreateExamActv.setAdapter(arrayAdapter)
        binding.fragmentCreateExamActv.setOnItemClickListener { adapterView, view, position, l ->
            viewModel.elimination = ELIMINATIONS.get(position)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.create_exam_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.create_exam) {
            alertDialogBuilder(
                requireContext(),
                "Emin misin?",
                "Sınavı kaydetmek istediğine emin misin?"
            ) {
                val result = viewModel.saveExam(requireView())
                if (result) {
                    val navOptions = NavOptions.Builder().setPopUpTo(R.id.examsFragment, true).build()
                    findNavController().navigate(CreateExamFragmentDirections.actionCreateExamFragmentToExamsFragment(), navOptions)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun timeClick() {
        val timeSetListener = object: TimePickerDialog.OnTimeSetListener{
            override fun onTimeSet(p0: TimePicker?, selectedHour: Int, selectedMinute: Int) {
                viewModel.selectedTime = makeMilliseconds(selectedHour, selectedMinute)
                binding.fragmentCreateExamTime.setText(viewModel.getTimeString())
            }
        }
        val style = AlertDialog.THEME_HOLO_DARK
        val map = makeTime(selectedTime)
        val timePickerDialog = TimePickerDialog(requireContext(), style, timeSetListener, map.get("hours") ?: 0, map.get("minutes") ?: 0, true)
        timePickerDialog.apply {
            setTitle("Sınav Süresi")
            show()
        }
    }

    private fun addLecture() {
        CreateLectureDialogFragment().apply {
            setCreateListener { lectureName, lectureQuestions ->
                viewModel.addLecture(lectureName, lectureQuestions)
            }
        }.show(parentFragmentManager, "create_lecture_fragment")
    }

}