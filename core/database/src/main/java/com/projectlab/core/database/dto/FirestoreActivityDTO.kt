package com.projectlab.core.database.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.projectlab.core.domain.entity.ActivityEntity
import com.projectlab.core.domain.model.EntityId
import java.time.Instant
import java.util.Date

/**
 * Data Transfer Object (DTO) for Firestore Activity.
 *
 * @property name The name of the activity.
 * @property latitude The latitude coordinate of the activity location.
 * @property longitude The longitude coordinate of the activity location.
 * @property activityDate The date of the activity.
 * @property description The details of the activity.
 * @property amount The amount associated with the activity, typically a price.
 * @property currencyCode The currency code for the amount, e.g., USD, EUR.
 *
 * @author ricardoceadev
 */

data class FirestoreActivityDTO (
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val activityDate: Timestamp = Timestamp.now(),
    val description: String = "",
    val amount: String = "",
    val currencyCode: String = "",
) {
    companion object {
        // Converts a domain model ActivityEntity to a FirestoreActivityDTO.
        @RequiresApi(Build.VERSION_CODES.O)
        fun fromDomain(
            domain: ActivityEntity,
            userDoc: DocumentReference,
            itineraryDoc: DocumentReference,
        ): FirestoreActivityDTO = FirestoreActivityDTO(
            name          = domain.name,
            latitude      = domain.latitude,
            longitude     = domain.longitude,
            activityDate  = Timestamp(Date.from(domain.activityDate)),
            description   = domain.description,
            amount        = domain.amount,
            currencyCode  = domain.currencyCode,
        )
    }
    // Converts a FirestoreActivityDTO to a domain model ActivityEntity.
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDomain(
        docId: String,
        userRef: EntityId,
        itineraryRef: EntityId
    ): ActivityEntity = ActivityEntity(
        id            = docId,
        name          = name,
        latitude      = latitude,
        longitude     = longitude,
        activityDate  = Instant.ofEpochMilli(activityDate.toDate().time),
        description   = description,
        amount        = amount,
        currencyCode  = currencyCode,
        userRef       = userRef,
        itineraryRef  = itineraryRef
    )
}