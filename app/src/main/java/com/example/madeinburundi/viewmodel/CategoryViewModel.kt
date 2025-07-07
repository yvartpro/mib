package com.example.madeinburundi.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.example.madeinburundi.data.model.Category
import com.example.madeinburundi.data.model.CategoryProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryProvider: CategoryProvider
) : ViewModel() {
    var categories = mutableStateListOf<Category>()
        private set
    init {
        loadCategories()
    }
    private fun loadCategories() {
        categories.clear()
        categories.addAll(categoryProvider.getCategories())
    }
    fun setActive(name: String?) {
        categories = categories.map {
            it.copy(isActive = it.name == name)
        }.toMutableStateList()
    }
}
