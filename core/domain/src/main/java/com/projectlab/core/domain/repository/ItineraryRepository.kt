package com.projectlab.core.domain.repository

import com.projectlab.core.domain.entity.ItineraryEntity
import com.projectlab.core.domain.model.EntityId
import kotlinx.coroutines.flow.Flow

/**
 * ItineraryRepository interface for itinerary-related operations.
 *
 * This interface defines the contract for itinerary-related data operations, including creating an
 * itinerary and retrieving an itinerary by its ID.
 *
 * @author ricardoceadev
 */

interface ItineraryRepository {
    suspend fun createItinerary(itinerary: ItineraryEntity): Result<EntityId>
    suspend fun getItineraryById(userId: String, itineraryId: String): Result<ItineraryEntity?>
    suspend fun getAllItineraries(userId: String): Result<List<ItineraryEntity>>
    suspend fun updateItinerary(itinerary: ItineraryEntity): Result<Unit>
    suspend fun deleteItinerary(userId: String, itineraryId: String): Result<Unit>
}