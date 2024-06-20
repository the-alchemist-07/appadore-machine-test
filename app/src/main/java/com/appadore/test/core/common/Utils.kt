package com.appadore.test.core.common

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import com.appadore.test.R
import com.appadore.test.data.dto.QuestionsDto
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

/**
 * Extension function read the questions json file from the assets folder.
 */
fun Context.readQuestionsFromJson(): QuestionsDto? {
    val moshi = Moshi.Builder()
//        .add(KotlinJsonAdapterFactory())
        .build()
    val adapter: JsonAdapter<QuestionsDto> = moshi.adapter(QuestionsDto::class.java)
    val questionsJson = this.assets.open("questions.json")
        .bufferedReader().use { it.readText() }
    return adapter.fromJson(questionsJson)
}

/**
 * Extension function to hide the soft keyboard.
 */
fun IBinder.hideKeyboard(context: Context) {
    try {
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Extension field which returns the flag's drawable id according to the country code.
 */
val String.flagDrawable: Int
    get() {
        return when (this.lowercase()) {
            "nz" -> R.drawable.flag_nz
            "aw" -> R.drawable.flag_aw
            "ec" -> R.drawable.flag_ec
            "py" -> R.drawable.flag_py
            "kg" -> R.drawable.flag_kg
            "pm" -> R.drawable.flag_pm
            "jp" -> R.drawable.flag_jp
            "tm" -> R.drawable.flag_tm
            "ga" -> R.drawable.flag_ga
            "mq" -> R.drawable.flag_mq
            "bz" -> R.drawable.flag_bz
            "cz" -> R.drawable.flag_cz
            "ae" -> R.drawable.flag_ae
            "je" -> R.drawable.flag_je
            "ls" -> R.drawable.flag_ls
            else -> R.drawable.flag_default
        }
    }
