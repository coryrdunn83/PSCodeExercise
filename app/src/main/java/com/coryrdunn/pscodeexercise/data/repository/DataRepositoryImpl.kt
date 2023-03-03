package com.coryrdunn.pscodeexercise.data.repository

import android.content.Context
import com.coryrdunn.pscodeexercise.data.data_source.getJsonDataFromFile
import com.coryrdunn.pscodeexercise.domain.model.DataBundle
import com.coryrdunn.pscodeexercise.domain.repository.DataRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepositoryImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context
): DataRepository {
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    private val _dataFlow = MutableStateFlow<DataBundle?>(null)
    override val dataFlow: StateFlow<DataBundle?> = _dataFlow

    init {
        scope.launch {
            getData()
        }
    }

    override suspend fun getData() {
        try {
            val jsonFileString = getJsonDataFromFile(applicationContext)
            val gson = Gson()
            val bundleType = object : TypeToken<DataBundle>() {}.type
            val dataBundle: DataBundle = gson.fromJson(jsonFileString, bundleType)

            _dataFlow.emit(dataBundle)
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }
}