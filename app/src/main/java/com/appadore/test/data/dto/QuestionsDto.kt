package com.appadore.test.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import androidx.annotation.Keep
import com.appadore.test.domain.model.Country
import com.appadore.test.domain.model.Question

@Keep
@JsonClass(generateAdapter = true)
data class QuestionsDto(
    @Json(name = "questions")
    val questions: List<QuestionDto>?
) {
    @Keep
    @JsonClass(generateAdapter = true)
    data class QuestionDto(
        @Json(name = "answer_id")
        val answerId: Int?,
        @Json(name = "countries")
        val countries: List<CountryDto>?,
        @Json(name = "country_code")
        val countryCode: String?
    ) {
        fun toQuestion(): Question {
            return Question(
                answerId = answerId ?: 0,
                countries = countries?.map { it.toCountry() } ?: emptyList(),
                countryCode = countryCode ?: ""
            )
        }

        @Keep
        @JsonClass(generateAdapter = true)
        data class CountryDto(
            @Json(name = "country_name")
            val countryName: String?,
            @Json(name = "id")
            val id: Int?
        ) {
            fun toCountry(): Country {
                return Country(
                    countryName = countryName ?: "",
                    id = id ?: 0
                )
            }
        }
    }
}
