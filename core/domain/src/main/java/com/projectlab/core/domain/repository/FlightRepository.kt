package com.projectlab.core.domain.repository

import com.projectlab.core.domain.entity.FlightEntity
import com.projectlab.core.domain.entity.FlightSegmentEntity
import com.projectlab.core.domain.model.EntityId
import kotlinx.coroutines.flow.Flow

/**
 * FlightRepository interface for flight-related operations.
 *
 * This interface defines the contract for flight-related data operations,
 * including creating a flight, retrieving flights by ID, and managing flight segments.
 *
 * @author ricardoceadev
 */

interface FlightRepository {
    // English: Operations on the main flight
    suspend fun createFlight(flight: FlightEntity): Result<EntityId>
    suspend fun getFlightById(userId: String, itinId: String, flightId: String): Result<FlightEntity?>
    suspend fun getAllFlights(userId: String, itinId: String): Result<List<FlightEntity>>
    suspend fun updateFlight(flight: FlightEntity): Result<Unit>
    suspend fun deleteFlight(userId: String, itinId: String, flightId: String): Result<Unit>

    // English: Operations on flight segments (located within a flight)
    suspend fun createFlightSegment(segment: FlightSegmentEntity): Result<EntityId>
    suspend fun getFlightSegmentById(
        userId: String,
        itinId: String,
        flightId: String,
        segmentId: String,
    ): Result<FlightSegmentEntity?>
    suspend fun getAllFlightSegments(
        userId: String,
        itinId: String,
        flightId: String,
    ): Result<List<FlightSegmentEntity>>
    suspend fun updateFlightSegment(segment: FlightSegmentEntity): Result<Unit>
    suspend fun deleteFlightSegment(
        userId: String,
        itinId: String,
        flightId: String,
        segmentId: String,
    ): Result<Unit>

}