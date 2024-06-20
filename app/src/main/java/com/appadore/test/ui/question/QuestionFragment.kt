package com.appadore.test.ui.question

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.appadore.test.R
import com.appadore.test.databinding.FragmentQuestionBinding

class QuestionFragment : Fragment(R.layout.fragment_question) {

    private lateinit var binding: FragmentQuestionBinding
    private val viewModel: QuestionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuestionBinding.bind(view)
    }

}