package bi.vovota.madeinburundi.data.model

import bi.vovota.madeinburundi.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryProvider @Inject constructor() {

    fun getCategories(): List<Category> = listOf(
        //Category(R.string.c_all, "tous", true),
        Category(R.string.c_food, "food", false),
        Category(R.string.c_beverage, "beverage", false),
        Category(R.string.c_soap, "soap", false),
        Category(R.string.c_beauty, "beauty", false),
        Category(R.string.c_electronics, "electronic", false),
        Category(R.string.c_construction, "construction", false)
    )
}
