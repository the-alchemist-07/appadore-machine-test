package com.appadore.test.ui.question

import com.appadore.test.domain.model.Question

sealed class QuestionState {
    data class ShowMessage(val message: String): QuestionState()
    data class UpdateQuestion(val question: Question): QuestionState()
    data class UpdateTimer(val timer: String): QuestionState()
}