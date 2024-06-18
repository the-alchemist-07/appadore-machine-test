package com.appadore.test.domain.model

data class Question(
    val answerId: Int,
    val countries: List<Country>,
    val countryCode: String
)