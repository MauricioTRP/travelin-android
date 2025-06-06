package com.projectlab.core.database.services

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.firestore.FirebaseFirestore
import com.projectlab.core.database.dto.FirestoreFlightDTO
import com.projectlab.core.database.dto.FirestoreFlightSegmentDTO
import com.projectlab.core.domain.model.EntityId
import com.projectlab.core.domain.entity.FlightEntity
import com.projectlab.core.domain.entity.FlightSegmentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * FirestoreFlightImpl is a concrete implementation of the FirestoreFlight interface.
 * It uses Firestore to perform CRUD operations on flight data.
 *
 * @param firestore The FirebaseFirestore instance used to interact with Firestore.
 */

class FirestoreFlightImpl @Inject constructor (
    private val firestore: FirebaseFirestore
) : FirestoreFlight {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createFlight(flight: FlightEntity): Result<EntityId> = runCatching {
        // user reference:
        var userDocRef = firestore
            .collection("Users")
            .document(flight.userRef?.value ?: throw IllegalArgumentException("userRef is null"))
        // itinerary reference:
        var itineraryDocRef = userDocRef
            .collection("Itineraries")
            .document(flight.itineraryRef?.value ?: throw IllegalArgumentException("itineraryRef is null"))
        // departure airport reference:
        var airportDepartureDocRef = firestore
            .collection("Airports")
            .document(flight.departureAirport["airportCodeRef"]?.toString() ?:
            throw IllegalArgumentException("airportDepartureCodeRef is null"))
        // departure airport reference:
        var airportArrivalDocRef = firestore
            .collection("Airports")
            .document(flight.departureAirport["airportCodeRef"]?.toString() ?:
            throw IllegalArgumentException("airportArrivalCodeRef is null"))
        // create dto:
        val dto = FirestoreFlightDTO.fromDomain(
            flight,
            userDocRef,
            itineraryDocRef,
            airportDepartureDocRef,
            airportArrivalDocRef
        )
        // add to firestore:
        val flightCol = itineraryDocRef.collection("Flights")
        val docRef = flightCol.document()
        docRef.set(dto).await()
        // return id:
        EntityId(docRef.id)

    }

    override suspend fun getFlightById(
        userId: String,
        itinId: String,
        flightId: String
    ): Result<FlightEntity?> = runCatching {
        // Get the document reference for the flight
        val docRef = firestore
            .collection("Users").document(userId)
            .collection("Itineraries").document(itinId)
            .collection("Flights").document(flightId)
        // we get the snapshot and map it to the DTO
        // If the snapshot exists, we map to domain, convert it to a FlightEntity and emit
        // Otherwise, we emit null
        val snap = docRef.get().await()
        if (snap.exists()) {
            val dto = snap.toObject(FirestoreFlightDTO::class.java)
            (
                dto?.toDomain(
                    docId = snap.id,
                    userRef = EntityId(userId),
                    itineraryRef = EntityId(itinId)
                )
            )
        } else {
            (null)
        }
    }

    override suspend fun getAllFlightsForItinerary(
        userId: String,
        itinId: String
    ): Result<List<FlightEntity>> = runCatching {
        // route to the Flights collection for the given user and itinerary
        val snaps = firestore
            .collection("Users").document(userId)
            .collection("Itineraries").document(itinId)
            .collection("Flights").get().await()

        // For each document, convert the DTO to Domain (FlightEntity):
        val list = snaps.documents.mapNotNull { doc ->
            doc.toObject(FirestoreFlightDTO::class.java)
                ?.toDomain(
                    docId = doc.id,
                    userRef = EntityId(userId),
                    itineraryRef = EntityId(itinId)
                )
        }
        (list)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateFlight(flight: FlightEntity): Result<Unit> = runCatching {
        // We retrieve the userId and itineraryId from the object reference
        val userId = flight.userRef?.value
            ?: throw IllegalArgumentException("userRef is null")
        val itineraryId = flight.itineraryRef?.value
            ?: throw IllegalArgumentException("itineraryRef is null")

        // Reconstruct the document routes:
        val userDocRef = firestore.collection("Users").document(userId)
        val itineraryDocRef = userDocRef.collection("Itineraries").document(itineraryId)

        // Convert to DTO:
        val dto = FirestoreFlightDTO.fromDomain(
            domain = flight,
            userDocRef = userDocRef,
            itineraryDocRef = itineraryDocRef,
            airportDepartureDocRef = firestore.collection("Airports")
                .document(flight.departureAirport["airportCodeRef"]?.toString()
                    ?: throw IllegalArgumentException("departureAirport.airportCodeRef is null")),
            airportArrivalDocRef = firestore.collection("Airports")
                .document(flight.arrivalAirport["airportCodeRef"]?.toString()
                    ?: throw IllegalArgumentException("arrivalAirport.airportCodeRef is null"))
        )

        // Overwrite, set() the specific flight document. TODO: handle errors.
        firestore.collection("Users").document(userId)
            .collection("Itineraries").document(itineraryId)
            .collection("Flights").document(flight.id)
            .set(dto).await()

    }

    override suspend fun deleteFlight(
        userId: String,
        itinId: String,
        flightId: String
    ): Result<Unit> = runCatching {
        firestore.collection("Users").document(userId)
            .collection("Itineraries").document(itinId)
            .collection("Flights").document(flightId)
            .delete().await()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createFlightSegment(flightSegment: FlightSegmentEntity
    ): Result<EntityId> = runCatching {
        // user reference:
        var userDocRef = firestore
            .collection("Users")
            .document(flightSegment.userRef?.value ?:
            throw IllegalArgumentException("userRef is null"))
        // itinerary reference:
        var itineraryDocRef = userDocRef
            .collection("Itineraries")
            .document(flightSegment.itineraryRef?.value ?:
            throw IllegalArgumentException("itineraryRef is null"))
        // flight reference:
        var flightDocRef = itineraryDocRef
            .collection("Flights")
            .document(flightSegment.flightRef?.value ?:
            throw IllegalArgumentException("flightRef is null"))
        // departure airport reference:
        var airportDepartureDocRef = firestore
            .collection("Airports")
            .document(flightSegment.departureAirportCodeRef?.toString() ?:
            throw IllegalArgumentException("airportDepartureCodeRef is null"))
        // arrival airport reference:
        var airportArrivalDocRef = firestore
            .collection("Airports")
            .document(flightSegment.departureAirportCodeRef?.toString() ?:
            throw IllegalArgumentException("airportArrivalCodeRef is null"))
        // create dto:
        val dto = FirestoreFlightSegmentDTO.fromDomain(
            flightSegment,
            userDocRef,
            itineraryDocRef,
            flightDocRef,
            airportDepartureDocRef,
            airportArrivalDocRef
        )
        // add to firestore:
        val flightSegmentCol = flightDocRef.collection("FlightSegments")
        val docRef = flightSegmentCol.document()
        docRef.set(dto).await()
        // return id:
        EntityId(docRef.id)
    }

    override suspend fun getFlightSegmentById(
        userId: String,
        itinId: String,
        flightId: String,
        segmentId: String
    ): Result<FlightSegmentEntity?> = runCatching {
        // Get the document reference for the flight segment
        val docRef = firestore
            .collection("Users").document(userId)
            .collection("Itineraries").document(itinId)
            .collection("Flights").document(flightId)
            .collection("FlightSegments").document(segmentId)

        // we get the snapshot and map it to the DTO
        // If the snapshot exists, we map to domain, convert it to a FlightSegmentEntity and emit
        // Otherwise, we emit null
        val snap = docRef.get().await()
        if (snap.exists()) {
            val dto = snap.toObject(FirestoreFlightSegmentDTO::class.java)
            (
                dto?.toDomain(
                    docId = snap.id,
                    userRef = EntityId(userId),
                    itineraryRef = EntityId(itinId),
                    flightRef = EntityId(flightId)
                )
            )
        } else {
            (null)
        }
    }

    override suspend fun getAllFlightSegmentsForFlight(
        userId: String,
        itinId: String,
        flightId: String
    ): Result<List<FlightSegmentEntity>> = runCatching {
        // Route to the FlightSegments collection for the given user, itinerary and flight
        val snaps = firestore
            .collection("Users").document(userId)
            .collection("Itineraries").document(itinId)
            .collection("Flights").document(flightId)
            .collection("FlightSegments").get().await()

        // For each document, convert the DTO to domain (FlightSegmentEntity):
        val list = snaps.documents.mapNotNull { doc ->
            doc.toObject(FirestoreFlightSegmentDTO::class.java)
                ?.toDomain(
                    docId = doc.id,
                    userRef = EntityId(userId),
                    itineraryRef = EntityId(itinId),
                    flightRef = EntityId(flightId)
                )
        }
        (list)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateFlightSegment(
        flightSegment: FlightSegmentEntity
    ): Result<Unit> = runCatching {
        // We retrieve the userId, itineraryId and flightId from the object reference
        val userId = flightSegment.userRef?.value
            ?: throw IllegalArgumentException("userRef is null")
        val itineraryId = flightSegment.itineraryRef?.value
            ?: throw IllegalArgumentException("itineraryRef is null")
        val flightId = flightSegment.flightRef?.value
            ?: throw IllegalArgumentException("flightRef is null")

        // Reconstruct the document routes/references:
        val userDocRef = firestore.collection("Users").document(userId)
        val itineraryDocRef = userDocRef.collection("Itineraries").document(itineraryId)
        val flightDocRef = itineraryDocRef.collection("Flights").document(flightId)

        // Convert to DTO:
        val dto = FirestoreFlightSegmentDTO.fromDomain(
            domain = flightSegment,
            userDocRef = userDocRef,
            itineraryDocRef = itineraryDocRef,
            flightDocRef = flightDocRef,
            airportDepartureDocRef = firestore.collection("Airports")
                .document(flightSegment.departureAirportCodeRef?.value
                    ?: throw IllegalArgumentException("departureAirportCodeRef is null")),
            airportArrivalDocRef = firestore.collection("Airports")
                .document(flightSegment.arrivalAirportCodeRef?.value
                    ?: throw IllegalArgumentException("arrivalAirportCodeRef is null"))
        )

        // Overwrite, set() the specific flight segment document. TODO: handle errors.
        firestore.collection("Users").document(userId)
            .collection("Itineraries").document(itineraryId)
            .collection("Flights").document(flightId)
            .collection("FlightSegments").document(flightSegment.id)
            .set(dto).await()
    }

    override suspend fun deleteFlightSegment(
        userId: String,
        itinId: String,
        flightId: String,
        segmentId: String
    ): Result<Unit> = runCatching {
        firestore.collection("Users").document(userId)
            .collection("Itineraries").document(itinId)
            .collection("Flights").document(flightId)
            .collection("FlightSegments").document(segmentId)
            .delete().await()
    }
}