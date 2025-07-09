package com.example.madeinburundi.data.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryProvider @Inject constructor() {

    fun getCategories(): List<Category> = listOf(
        Category("Tous", "tous", true),
        Category("Aliments", "food", false),
        Category("Boissons", "beverage", false),
        Category("Sovons", "soap", false),
        Category("Beauté", "beauty", false),
        Category("Electroniques", "electronic", false),
        Category("Construction", "construction", false)
    )
}
