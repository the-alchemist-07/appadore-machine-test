package com.appadore.test.data.repository

import android.app.Application
import com.appadore.test.core.common.Resource
import com.appadore.test.core.common.readQuestionsFromJson
import com.appadore.test.domain.model.Question
import com.appadore.test.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class QuestionRepositoryImpl(
    private val application: Application
) : QuestionRepository {

    override fun saveScheduledTime(hour: Int, minute: Int, second: Int) {
        
    }

    override fun getQuestionsList(): Flow<Resource<List<Question>>> = flow {
        val questionsDto = application.readQuestionsFromJson()
        if (questionsDto != null && !questionsDto.questions.isNullOrEmpty()) {
            val questionsList = questionsDto.questions.map { it.toQuestion() }
            emit(Resource.Success(questionsList))
        } else emit(Resource.Error(EMPTY_QUESTIONS))
    }



    companion object {
        const val EMPTY_QUESTIONS = "No questions found!"
    }
}