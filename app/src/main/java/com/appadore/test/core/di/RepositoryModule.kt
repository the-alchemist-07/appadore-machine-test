package com.appadore.test.core.di

import com.appadore.test.data.repository.QuestionRepositoryImpl
import com.appadore.test.domain.repository.QuestionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindQuestionRepository(questionRepositoryImpl: QuestionRepositoryImpl): QuestionRepository

}