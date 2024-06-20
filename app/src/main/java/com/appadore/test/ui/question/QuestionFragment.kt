package com.appadore.test.ui.question

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.appadore.test.R
import com.appadore.test.core.common.flagDrawable
import com.appadore.test.core.common.hideKeyboard
import com.appadore.test.databinding.FragmentQuestionBinding
import com.appadore.test.domain.model.Country
import com.appadore.test.domain.model.Question
import com.appadore.test.ui.question.adapters.CountryAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuestionFragment : Fragment(R.layout.fragment_question), CountryAdapter.OnClickListener {

    private lateinit var binding: FragmentQuestionBinding
    private val viewModel: QuestionViewModel by viewModels()
    private val adapter by lazy { CountryAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuestionBinding.bind(view)

        setupRecyclerView()
        observeState()
    }

    private fun setupRecyclerView() {
        binding.recyclerOptions.adapter = adapter
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is QuestionState.ShowMessage -> showMessage(state.message)
                        is QuestionState.UpdateTimer -> updateTimerUi(state.timer)
                        is QuestionState.UpdateQuestion -> updateQuestion(state.question)
                    }
                }
            }
        }
    }

    private fun showMessage(message: String) {
        binding.root.windowToken.hideKeyboard(requireContext())
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun updateTimerUi(countdown: String) {
        binding.tvTimer.text = countdown
    }

    private fun updateQuestion(question: Question) {
        with(binding) {
            val questionNo = viewModel.questionNumber + 1
            tvQuestionNumber.text = questionNo.toString()
            ivFlag.setImageResource(question.countryCode.flagDrawable)
        }

        adapter.answerId = question.answerId
        adapter.submitList(question.countries)
    }

    override fun onCountryClicked(country: Country) {
        showMessage(country.countryName)
        // TODO: Handle the score and update it in datastore preferences...
    }
}
