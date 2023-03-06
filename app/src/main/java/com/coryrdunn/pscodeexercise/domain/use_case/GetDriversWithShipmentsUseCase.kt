package com.coryrdunn.pscodeexercise.domain.use_case

import com.coryrdunn.pscodeexercise.domain.model.DriverWithShipment
import com.coryrdunn.pscodeexercise.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDriversWithShipmentsUseCase @Inject constructor(
    private val dataRepository: DataRepository
) {
    operator fun invoke(): Flow<List<DriverWithShipment>> = flow {

        dataRepository.getData().also { dataBundle ->

            val drivers = dataBundle.drivers.toMutableList()
            val shipments = dataBundle.shipments
            val driversWithShipments = mutableListOf<DriverWithShipment>()

            shipments.forEach { shipment ->
                val driver = getDriverForShipment(drivers, shipment)
                driversWithShipments.add(DriverWithShipment(driver, shipment))
                drivers.remove(driver)
            }

            emit(driversWithShipments.toList())
        }
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