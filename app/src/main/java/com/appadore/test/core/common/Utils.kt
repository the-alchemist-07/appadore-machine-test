package com.appadore.test.core.common

import android.content.Context
import com.appadore.test.data.dto.QuestionsDto
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

fun Context.readQuestionsFromJson(): QuestionsDto? {
    val moshi = Moshi.Builder()
//        .add(KotlinJsonAdapterFactory())
        .build()
    val adapter: JsonAdapter<QuestionsDto> = moshi.adapter(QuestionsDto::class.java)
    val questionsJson = this.assets.open("questions.json")
        .bufferedReader().use { it.readText() }
    return adapter.fromJson(questionsJson)
}