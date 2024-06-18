package com.appadore.test.domain.repository

import com.appadore.test.core.common.Resource
import com.appadore.test.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {

    fun saveScheduledTime(hour: Int, minute: Int, second: Int)

    fun getQuestionsList(): Flow<Resource<List<Question>>>

}