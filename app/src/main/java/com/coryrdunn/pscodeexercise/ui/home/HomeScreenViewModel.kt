package com.coryrdunn.pscodeexercise.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coryrdunn.pscodeexercise.domain.model.DataBundle
import com.coryrdunn.pscodeexercise.domain.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    dataRepository: DataRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dataRepository.dataFlow.collectLatest { data ->
                _uiState.update { currentState ->
                    currentState.copy(dataBundle = data)
                }
            }
        }
    }

    fun toggleDialog(driver: String? = null) {
        _uiState.update { currentState ->
            currentState.copy(
                showDialog = !currentState.showDialog,
                selectedDriver = driver
            )
        }
    }

}

data class HomeScreenUiState(
    val dataBundle: DataBundle? = null,
    val showDialog: Boolean = false,
    val selectedDriver: String? = null
) {
    val driverList: List<String>
        get() = dataBundle?.drivers ?: emptyList()

    val shipmentList: List<String>
        get() = dataBundle?.shipments ?: emptyList()

    val selectedDriverShipment: String?
        get() {
            val ssScoreMap = mutableMapOf<Double, String>()
            val vowelsList = listOf('a','e','i','o','u')

            shipmentList.forEach { shipment ->
                val words = shipment.split(" ")
                val startIndex = words.indexOfFirst { it.matches(Regex("\\d+")) } + 1
                val endIndex = words.indexOfFirst { it.matches(Regex("(Apt\\.|Suite\\.)")) }
                val streetName = words.subList(
                    startIndex,
                    if (endIndex != -1) endIndex else words.size
                ).joinToString(" ")

                var baseScore = 0.00
                var vowelsCount = 0.0
                selectedDriver?.lowercase()?.forEach {
                    if (vowelsList.contains(it)) vowelsCount++
                }
                if(streetName.length % 2 == 0) {
                    baseScore = vowelsCount * 1.5
                } else {
                    baseScore = selectedDriver?.length?.toDouble()?.minus(vowelsCount) ?: 0.0
                }

                // todo: Check for common factors to increase SS by 50%

                ssScoreMap.put(baseScore, shipment)
            }

            return dataBundle?.shipments?.firstOrNull()
        }
}