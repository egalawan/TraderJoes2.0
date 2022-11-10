package com.example.traderjoes20

data class Recipe(
    val cookingTime: String,
    val directions: String,
    val id: String,
    val img: String,
    val ingredients: List<String>,
    val prepTime: String,
    val serves: String,
    val tagIds: List<Int>,
    val tags: List<Tag>,
    val title: String
)