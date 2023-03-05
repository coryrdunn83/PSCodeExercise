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
                selectedDriver = currentState.driversWithShipmentsList.firstOrNull { it.driver == driver }
            )
        }
    }

}

data class HomeScreenUiState(
    private val dataBundle: DataBundle? = null,
    val showDialog: Boolean = false,
    val selectedDriver: DriverWithShipment? = null
) {
    data class DriverWithShipment (
        val driver: String = "",
        val shipment: String = ""
    )

    val driverList: List<String>
        get() = dataBundle?.drivers ?: emptyList()

    private val shipmentList: List<String>
        get() = dataBundle?.shipments ?: emptyList()

    val driversWithShipmentsList: List<DriverWithShipment>
        get() {
            val drivers = driverList.toMutableList()
            val driversWithShipments = mutableListOf<DriverWithShipment>()

            shipmentList.forEach { shipment ->
                val driver = getDriverForShipment(drivers, shipment)
                driversWithShipments.add(DriverWithShipment(driver, shipment))
                drivers.remove(driver)
            }

            return driversWithShipments
        }

    private fun getDriverForShipment(drivers: MutableList<String>, shipment: String): String {
        val sScoreMap = mutableMapOf<Double, String>()

        drivers.forEach { driver ->
            val streetName = getStreetName(shipment)
            val driverLength = driver.length
            val streetLength = streetName.length
            var baseScore = getBaseSuitabilityScore(streetLength, driver)

            for (i in 2..minOf(streetLength, driverLength)) {
                if (streetLength % i == 0 && driverLength % i == 0) {
                    baseScore *= 1.5
                    break
                }
            }

            sScoreMap[baseScore] = driver
        }

        return sScoreMap.maxBy { it.key }.value
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

    private fun getBaseSuitabilityScore(streetLength: Int, driver: String): Double {
        var vowelsCount = 0.0
        val vowelsList = listOf('a','e','i','o','u')

        driver.lowercase().forEach {
            if (vowelsList.contains(it)) vowelsCount++
        }

        return if(streetLength % 2 == 0) {
            vowelsCount * 1.5
        } else {
            driver.length.minus(vowelsCount)
        }
    }
}