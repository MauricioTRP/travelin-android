package com.projectlab.core.database.services

import com.projectlab.core.domain.entity.ItineraryEntity
import com.projectlab.core.domain.model.EntityId
import kotlinx.coroutines.flow.Flow
import kotlin.Unit;

/**
 * FirestoreItinerary interface for itinerary-related operations.
 *
 * This interface defines the contract for itinerary-related data operations, including creating an
 * itinerary and retrieving an itinerary by its ID.
 *
 * @author ricardoceadev
 */

interface FirestoreItinerary {
    suspend fun createItinerary(itinerary: ItineraryEntity): Result<EntityId>
    suspend fun getItinerariesById(userId: String, itineraryId: String): Result<ItineraryEntity?>
    suspend fun getAllItinerariesForUser(userId: String): Result<List<ItineraryEntity>>
    suspend fun updateItinerary(itinerary: ItineraryEntity): Result<Unit>
    suspend fun deleteItinerary(userId: String, itineraryId: String): Result<Unit>
}
