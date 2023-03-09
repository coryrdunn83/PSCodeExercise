package com.coryrdunn.pscodeexercise.domain.use_case

import com.coryrdunn.pscodeexercise.domain.model.DriverWithShipment
import com.coryrdunn.pscodeexercise.domain.repository.DataRepository
import com.coryrdunn.pscodeexercise.domain.util.DriverWithShipmentUtil.getDriverForShipment
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
}