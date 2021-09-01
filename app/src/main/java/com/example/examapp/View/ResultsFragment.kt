package com.example.examapp.View

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.arraySetOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examapp.Adapter.ResultAdapter
import com.example.examapp.R
import com.example.examapp.Util.Util
import com.example.examapp.ViewModel.ResultsViewModel
import com.example.examapp.databinding.FragmentResultsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class ResultsFragment : Fragment(R.layout.fragment_results) {

    private lateinit var binding: FragmentResultsBinding
    private val viewModel: ResultsViewModel by viewModels()
    private val recyclerAdapter = ResultAdapter()
    private val exams = arraySetOf<String>("Hepsi")

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentResultsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.fragmentResultToolbar)
        binding.fragmentResultToolbar.title = "Sınav Sonuçları"
        setHasOptionsMenu(true)

        setupRv()
        collectData()
    }

    override fun onResume() {
        buildActv()
        super.onResume()
    }

    @ExperimentalCoroutinesApi
    private fun collectData() {
        viewModel.results.observe(viewLifecycleOwner) {
            binding.fragmentResultPb.isVisible = false
            recyclerAdapter.list = it

            if (exams.size == 1) {
                it.forEach {
                    exams.add(it.examResult.examName)
                }
                buildActv()
            }
        }
    }

    private fun setupRv() {
        binding.fragmentResultRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }
        recyclerAdapter.setOnExamResultClickListener { result ->
            findNavController().navigate(ResultsFragmentDirections.actionResultsFragmentToExamResultDetailFragment(result))
        }
    }

    private fun buildActv() {
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, exams.toArray())
        binding.fragmentResultActv.setAdapter(arrayAdapter)
        binding.fragmentResultActv.setOnItemClickListener { adapterView, view, position, l ->
            val selection = exams.toList().get(position)
            val name = if (selection == "Hepsi") null else selection
            viewModel.updateExamName(name)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sorting_menu, menu)
        menu.findItem(R.id.sort_by_date).setChecked(true)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_by_date -> viewModel.updateSortType(Util.SORT_TYPES.SORT_BY_DATE)
            R.id.sort_by_total -> viewModel.updateSortType(Util.SORT_TYPES.SORT_BY_TOTAL)
        }
        if (!item.isChecked) {
            binding.fragmentResultPb.isVisible = true
            item.setChecked(true)
        }

        return super.onOptionsItemSelected(item)
    }

}