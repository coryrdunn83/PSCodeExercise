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

    val selectedDriverShipment: String
        get() {
            val ssScoreMap = mutableMapOf<Double, String>()


            shipmentList.forEach { shipment ->
                val streetName = getStreetName(shipment)
                val driverLength = selectedDriver?.length ?: 0
                val streetLength = streetName.length
                var baseScore = getBaseSuitabilityScore(streetLength, driverLength)

                for (i in 2..minOf(streetLength, driverLength)) {
                    if (streetLength % i == 0 && driverLength % i == 0) {
                        baseScore *= 1.5
                    }
                }

                ssScoreMap[baseScore] = shipment
            }

            return ssScoreMap.maxBy { it.key }.value
        }

    private fun getStreetName(shipment: String): String {
        val words = shipment.split(" ")
        val startIndex = words.indexOfFirst { it.matches(Regex("\\d+")) } + 1
        val endIndex = words.indexOfFirst { it.matches(Regex("(Apt\\.|Suite\\.)")) }
        return words.subList(
            startIndex,
            if (endIndex != -1) endIndex else words.size
        ).joinToString(" ")
    }

    private fun getBaseSuitabilityScore(streetLength: Int, driverLength: Int): Double {
        var vowelsCount = 0.0
        val vowelsList = listOf('a','e','i','o','u')

        selectedDriver?.lowercase()?.forEach {
            if (vowelsList.contains(it)) vowelsCount++
        }
        return if(streetLength % 2 == 0) {
            vowelsCount * 1.5
        } else {
            driverLength.toDouble().minus(vowelsCount)
        }
    }
}