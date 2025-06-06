package com.projectlab.core.data.repository

import com.projectlab.core.database.services.FirestoreFlight
import com.projectlab.core.database.services.FirestoreFlightImpl
import com.projectlab.core.domain.entity.FlightEntity
import com.projectlab.core.domain.entity.FlightSegmentEntity
import com.projectlab.core.domain.model.EntityId
import com.projectlab.core.domain.repository.FlightRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * FlightRepositoryImpl is the implementation of the FlightRepository interface.
 * It performs operations on flights using services.
 *
 * @param firestoreFlight The FirestoreFlight service used for database operations.
 */

class FlightRepositoryImpl @Inject constructor (
    private val firestoreFlight: FirestoreFlight
) : FlightRepository {
    override suspend fun createFlight(flight: FlightEntity): Result<EntityId> {
        return firestoreFlight.createFlight(flight)
    }

    override suspend fun getFlightById(
        userId: String,
        itinId: String,
        flightId: String
    ): Result<FlightEntity?> {
        return firestoreFlight.getFlightById(userId, itinId, flightId)
    }

    override suspend fun getAllFlights(
        userId: String,
        itinId: String
    ): Result<List<FlightEntity>> {
        return firestoreFlight.getAllFlightsForItinerary(userId, itinId)
    }

    override suspend fun updateFlight(flight: FlightEntity): Result<Unit> {
        return firestoreFlight.updateFlight(flight)
    }

    override suspend fun deleteFlight(
        userId: String,
        itinId: String,
        flightId: String
    ): Result<Unit> {
        return firestoreFlight.deleteFlight(userId, itinId, flightId)
    }

    override suspend fun createFlightSegment(segment: FlightSegmentEntity): Result<EntityId> {
        return firestoreFlight.createFlightSegment(segment)
    }

    override suspend fun getFlightSegmentById(
        userId: String,
        itinId: String,
        flightId: String,
        segmentId: String
    ): Result<FlightSegmentEntity?> {
        return firestoreFlight.getFlightSegmentById(userId, itinId, flightId, segmentId)
    }

    override suspend fun getAllFlightSegments(
        userId: String,
        itinId: String,
        flightId: String
    ): Result<List<FlightSegmentEntity>> {
        return firestoreFlight.getAllFlightSegmentsForFlight(userId, itinId, flightId)
    }

    override suspend fun updateFlightSegment(segment: FlightSegmentEntity): Result<Unit> {
        return firestoreFlight.updateFlightSegment(segment)
    }

    override suspend fun deleteFlightSegment(
        userId: String,
        itinId: String,
        flightId: String,
        segmentId: String
    ): Result<Unit> {
        return firestoreFlight.deleteFlightSegment(userId, itinId, flightId, segmentId)
    }

}