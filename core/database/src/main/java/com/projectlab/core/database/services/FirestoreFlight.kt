package com.projectlab.core.database.services

import com.projectlab.core.domain.entity.FlightEntity
import com.projectlab.core.domain.entity.FlightSegmentEntity
import com.projectlab.core.domain.model.EntityId
import kotlinx.coroutines.flow.Flow

/**
 * FirestoreFlight interface for flight-related operations.
 *
 * This interface defines the contract for flight-related data operations, including creating a flight
 * and retrieving a flight by its ID.
 *
 * @author ricardoceadev
 */

interface FirestoreFlight {
    suspend fun createFlight(flight: FlightEntity): Result<EntityId>
    suspend fun getFlightById(userId: String, itinId: String, flightId: String): Result<FlightEntity?>
    suspend fun getAllFlightsForItinerary(userId: String, itinId: String): Result<List<FlightEntity>>
    suspend fun updateFlight(flight: FlightEntity): Result<Unit>
    suspend fun deleteFlight(userId: String, itinId: String, flightId: String): Result<Unit>
    suspend fun createFlightSegment(flightSegment: FlightSegmentEntity): Result<EntityId>
    suspend fun getFlightSegmentById(
        userId: String,
        itinId: String,
        flightId: String,
        segmentId: String
    ): Result<FlightSegmentEntity?>
    suspend fun getAllFlightSegmentsForFlight(
        userId: String,
        itinId: String,
        flightId: String
    ): Result<List<FlightSegmentEntity>>
    suspend fun updateFlightSegment(flightSegment: FlightSegmentEntity): Result<Unit>
    suspend fun deleteFlightSegment(
        userId: String,
        itinId: String,
        flightId: String,
        segmentId: String
    ): Result<Unit>
}