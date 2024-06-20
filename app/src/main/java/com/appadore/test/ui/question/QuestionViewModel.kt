package com.appadore.test.ui.question

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appadore.test.core.common.Constants.INTERVAL_QUESTION
import com.appadore.test.core.common.Constants.INTERVAL_RELAX
import com.appadore.test.core.common.Constants.ONE_SECOND
import com.appadore.test.core.common.Resource
import com.appadore.test.domain.model.Question
import com.appadore.test.domain.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val questionRepository: QuestionRepository
) : ViewModel() {

    private var questionList: List<Question> = emptyList()
    var questionNumber: Int = 0

    private var questionTimer: CountDownTimer? = null
//    private var relaxTimer: CountDownTimer? = null

    private val _state = Channel<QuestionState>()
    val state = _state.receiveAsFlow()

    init {
        getQuestionsList()
    }

    private fun getQuestionsList() = viewModelScope.launch {
        questionRepository.getQuestionsList().cancellable().collectLatest { state ->
            when (state) {
                is Resource.Success -> {
                    questionList = state.value
                    if (questionList.isNotEmpty())
                        startQuestionTimer(questionNumber)
                }

                is Resource.Error -> _state.send(QuestionState.ShowMessage(state.error))
                else -> Unit
            }
        }
    }

    private fun processQuestion(questionNumber: Int): Boolean {
        if (questionNumber <= questionList.size) {
            viewModelScope.launch {
                _state.send(QuestionState.UpdateQuestion(questionList[questionNumber]))
            }
            return true
        } else {
            // All questions are over.
            return false
        }
    }

    private fun startQuestionTimer(questionNumber: Int) {
        val startTimer = processQuestion(questionNumber)

        if (startTimer) {
            object : CountDownTimer(INTERVAL_QUESTION, ONE_SECOND) {
                override fun onTick(m: Long) {
                    val seconds = m / 1000
                    val timer = if (seconds < 10) "00:0$seconds" else "00:$seconds"
                    viewModelScope.launch { _state.send(QuestionState.UpdateTimer(timer)) }
                }

                override fun onFinish() {
                    startRelaxTimer()
                    // TODO: Answer can also be processed in here, to be checked...
                }
            }.start()
        }
    }

    private fun startRelaxTimer() = object : CountDownTimer(INTERVAL_RELAX, ONE_SECOND) {
        override fun onTick(m: Long) {
            val seconds = m / 1000
            val timer = if (seconds < 10) "00:0$seconds" else "00:$seconds"
            viewModelScope.launch { _state.send(QuestionState.UpdateTimer(timer)) }
        }

        override fun onFinish() {
            startQuestionTimer(++questionNumber)
        }
    }.start()

    fun stopQuestionTimerOnAnswerSelected() {
        questionTimer?.onFinish()
    }
}