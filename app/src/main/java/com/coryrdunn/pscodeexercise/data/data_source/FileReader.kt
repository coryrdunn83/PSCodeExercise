package com.coryrdunn.pscodeexercise.data.data_source

import android.content.Context
import com.coryrdunn.pscodeexercise.R
import java.io.IOException

fun getJsonDataFromFile(context: Context): String? {
    val jsonString: String
    try {
        jsonString = context.resources.openRawResource(R.raw.ps_data).bufferedReader().use {
            it.readText()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
    return jsonString
}