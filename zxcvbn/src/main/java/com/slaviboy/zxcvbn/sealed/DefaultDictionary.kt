package com.slaviboy.zxcvbn.sealed

import androidx.annotation.RawRes

sealed class DefaultDictionary(
    val name: String,
    @RawRes val fileResId: Int
) {
    /*object usTvAndFilm : DefaultDictionary("us_tv_and_film", R.raw.us_tv_and_film)
    object englishWikipedia : DefaultDictionary("english_wikipedia", R.raw.english_wikipedia)
    object passwords : DefaultDictionary("passwords", R.raw.passwords)
    object surnames : DefaultDictionary("surnames", R.raw.surnames)
    object maleNames : DefaultDictionary("male_names", R.raw.male_names)
    object femaleNames : DefaultDictionary("male_names", R.raw.female_names)*/
}