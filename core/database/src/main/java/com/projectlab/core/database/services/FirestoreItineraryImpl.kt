package com.projectlab.core.database.services

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.projectlab.core.database.dto.FirestoreItineraryDTO
import com.projectlab.core.domain.model.EntityId
import com.projectlab.core.domain.entity.ItineraryEntity
import com.projectlab.core.domain.repository.UserSessionProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * FirestoreItineraryImpl is a concrete implementation of the FirestoreItinerary interface.
 * It uses Firebase Firestore to perform CRUD operations on itinerary data.
 *
 * @param firestore The FirebaseFirestore instance used to interact with Firestore.
 */

class FirestoreItineraryImpl @Inject constructor (
    private val firestore: FirebaseFirestore,
    private val userSessionProvider: UserSessionProvider,
) : FirestoreItinerary {
    private val usersCol = firestore.collection("Users")

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createItinerary(
        itinerary: ItineraryEntity
    ): Result<EntityId> = runCatching {
        // Get the userId from the session provider
        val userId = userSessionProvider.getUserSessionId()
            ?: throw NullPointerException("userId is null")
        // user reference:
        var userDocRef = usersCol.document(userId)
        // create dto:
        val dto = FirestoreItineraryDTO.fromDomain(itinerary)
        // add to firestore:
        val itCol = userDocRef.collection("Itineraries")
        val docRef = itCol.document()
        docRef.set(dto).await()
        // return id:
        EntityId(docRef.id)
    }

    override suspend fun getItinerariesById(
        itineraryId: String,
    ): Result<ItineraryEntity?> = runCatching {
        // Get the userId from the session provider
        val userId = userSessionProvider.getUserSessionId()
            ?: throw NullPointerException("userId is null")
        // we get the snapshot and map it to the DTO
        // If the snapshot exists, we map to domain, convert it to a ItineraryEntity and emit
        // Otherwise, we emit null
        val snap = usersCol.document(userId)
            .collection("Itineraries").document(itineraryId)
            .get().await()
        if (snap.exists()) {
            val dto = snap.toObject(FirestoreItineraryDTO::class.java)
            (dto?.toDomain(
                docId = snap.id,
                userRef =  EntityId(userId))
            )
        } else {
            (null)
        }
    }

    override suspend fun getAllItinerariesForUser(
        userId: String
    ): Result<List<ItineraryEntity>> = runCatching {
        // Get the userId from the session provider
        val userId = userSessionProvider.getUserSessionId()
            ?: throw NullPointerException("userId is null")
        // Fetch all itineraries for a user
        // Route to the itineraries collection of the user
        val snaps = usersCol.document(userId)
            .collection("Itineraries").get().await()

        //For each document, convert the DTO to a domain (ItineraryEntity):
        val list = snaps.documents.mapNotNull { doc ->
            doc.toObject(FirestoreItineraryDTO::class.java)
                ?.toDomain(
                    docId =  doc.id,
                    userRef = EntityId(userId)
                )
        }
        (list)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateItinerary(
        itinerary: ItineraryEntity,
    ): Result<Unit> = runCatching {
        // Get the userId from the session provider
        val userId = userSessionProvider.getUserSessionId()
            ?: throw NullPointerException("userId is null")

        // Reconstruct the document routes/references:
        val userDocRef = usersCol.document(userId) // we don't used it still

        // convert to dto:
        val dto = FirestoreItineraryDTO.fromDomain(
            domain = itinerary
        )

        // Overwrite, set() the specific itinerary document. TODO: handle errors.
        // usersCol.document(userId)
        userDocRef
            .collection("Itineraries").document(itinerary.id)
            .set(dto).await()
    }

    override suspend fun deleteItinerary(
        itineraryId: String
    ): Result<Unit> = runCatching {
        // Get the userId from the session provider
        val userId = userSessionProvider.getUserSessionId()
            ?: throw NullPointerException("userId is null")
        // Delete the itinerary document for the user
        usersCol.document(userId)
            .collection("Itineraries").document(itineraryId)
            .delete().await()
    }
}