package com.example.examapp.View

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.examapp.R
import com.example.examapp.Util.Util
import com.example.examapp.databinding.FragmentWelcomeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentWelcomeBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        checkUser()
        binding.loginButton.setOnClickListener {
            val userName = binding.userNameInput.text.toString()
            if (userName.isNotEmpty()) {
                saveUser(userName)
            } else {
                Snackbar.make(requireView(), "Lütfen isminizi giriniz.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkUser() {
        lifecycleScope.launch {
            val userName = Util.getUserName(requireContext())
            userName?.let {
                navigateToMainFragment()
            } ?: kotlin.run {
                binding.loginLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun saveUser(userName: String) {
        lifecycleScope.launch {
            Util.writeUserName(requireContext(), userName)
            navigateToMainFragment()
        }
    }

    private fun navigateToMainFragment() {
        /*
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.welcomeFragment, true)
            .build()
        findNavController().navigate(
            R.id.action_welcomeFragment_to_mainFragment,
            null,
            navOptions
        )
        */
    }

}