package com.appadore.test.ui.schedule

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.appadore.test.R
import com.appadore.test.core.common.hideKeyboard
import com.appadore.test.databinding.FragmentScheduleBinding
import com.google.android.material.snackbar.Snackbar
import com.otpview.OTPListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScheduleFragment : Fragment(R.layout.fragment_schedule) {

    private lateinit var binding: FragmentScheduleBinding
    private val viewModel: ScheduleViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentScheduleBinding.bind(view)

        setListeners()
        observeState()
    }

    private fun setListeners() = binding.apply {
        boxViewHour.otpListener = object : OTPListener {
            override fun onInteractionListener() {}
            override fun onOTPComplete(otp: String) {
                viewModel.hour = (otp.ifBlank { 0 }).toString().toInt()
                boxViewMinute.requestFocus()
            }
        }

        boxViewMinute.otpListener = object : OTPListener {
            override fun onInteractionListener() {}
            override fun onOTPComplete(otp: String) {
                viewModel.minute = (otp.ifBlank { 0 }).toString().toInt()
                boxViewSecond.requestFocus()
            }
        }

        boxViewSecond.otpListener = object : OTPListener {
            override fun onInteractionListener() {}
            override fun onOTPComplete(otp: String) {
                viewModel.second = (otp.ifBlank { 0 }).toString().toInt()
            }
        }

        btnSave.setOnClickListener {
            viewModel.validateTime()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is ScheduleState.ShowMessage -> showMessage(state.message)
                        is ScheduleState.OnChallengeTickStarted -> updateChallengeCounter(state.countdown)
                        is ScheduleState.OnTimeScheduled -> onTimeScheduled()
                    }
                }
            }
        }
    }

    private fun showMessage(message: String) {
        binding.root.windowToken.hideKeyboard(requireContext())
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun onTimeScheduled() {
        with(binding) {
            boxViewHour.setOTP("")
            boxViewMinute.setOTP("")
            boxViewSecond.setOTP("")
            binding.root.windowToken.hideKeyboard(requireContext())
            showMessage(getString(R.string.schedule_success_message))
        }
    }

    private fun updateChallengeCounter(countdown: Int) {
        if (binding.cardSchedule.isVisible) {
            binding.cardSchedule.isVisible = false
            binding.cardCountdown.isVisible = true
        }

        val arrangedCounter = if (countdown < 10) "00:0$countdown" else "00:$countdown"
        binding.tvCountdownTimer.text = arrangedCounter

        if (countdown == 0) {
            findNavController().navigate(
                ScheduleFragmentDirections.actionScheduleFragmentToQuestionFragment(1)
            )
        }
    }

    /*private fun hideKeyboard() {
        try {
            val inputMethodManager =
                requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/
}