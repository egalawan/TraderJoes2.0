package com.example.traderjoes20

class Recipes {
    var tagid: Int? = null
    var name: String? = null
    var ingredients: List<String>? = null
    var servings: Int? = null
    override fun toString(): String {
        return "User{" +
                "name='" + name + '\'' +
                ", tags=" + tagid +
                ", messages=" + ingredients +
                ", servings= " + servings
                "}"
    }
}
