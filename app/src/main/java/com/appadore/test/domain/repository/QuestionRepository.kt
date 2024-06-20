package com.appadore.test.domain.repository

import com.appadore.test.core.common.Resource
import com.appadore.test.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {

    suspend fun saveScheduledTime(timeInMillis: Long)

    fun getQuestionsList(): Flow<Resource<List<Question>>>

}