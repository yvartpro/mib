package bi.vovota.madeinburundi.data.model

import androidx.annotation.DrawableRes
import bi.vovota.madeinburundi.R

data class Country(
    val name: String,
    val code: String,
    @DrawableRes val flag: Int,
    val initial: String,
    val numberLength: Int
)
val countryList = listOf(
    Country("Burundi", "257", R.drawable.bi, "BDI", 8),
    Country("DRC", "243", R.drawable.cd, "RDC", 9),
    Country("Kenya", "254", R.drawable.ke, "KE", 9),
    Country("Ouganda", "256", R.drawable.ug, "UG", 9),
    Country("Rwanda", "250", R.drawable.rw, "RW", 9),
    Country("Tanzania", "255", R.drawable.tz, "TZ", 9),
    Country("South Soudan", "211", R.drawable.ss, "SS", 9),
    Country("Somalia", "252", R.drawable.so, "SO", 9)
)