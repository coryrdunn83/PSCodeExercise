package com.coryrdunn.pscodeexercise.data.repository

import android.content.Context
import com.coryrdunn.pscodeexercise.data.data_source.getJsonDataFromFile
import com.coryrdunn.pscodeexercise.data.model.DataBundle
import com.coryrdunn.pscodeexercise.domain.repository.DataRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepositoryImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context
): DataRepository {

    override suspend fun getData(): DataBundle {
        try {
            val jsonFileString = getJsonDataFromFile(applicationContext)
            val gson = Gson()
            val bundleType = object : TypeToken<DataBundle>() {}.type

            return gson.fromJson(jsonFileString, bundleType)
        } catch (error: Exception) {
            error.printStackTrace()
        }
        return DataBundle()
    }
}