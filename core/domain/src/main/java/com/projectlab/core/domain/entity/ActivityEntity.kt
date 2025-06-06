package com.projectlab.core.domain.entity

import com.projectlab.core.domain.model.EntityId
import java.time.Instant

/**
 * ActivityEntity represents an activity in the database system inside the app.
 * @property id Unique identifier for the activity.
 * @property name Name of the activity.
 * @property latitude Latitude coordinate of the activity location.
 * @property longitude Longitude coordinate of the activity location.
 * @property activityDate Date of the activity.
 * @property description Details about the activity.
 * @property amount Amount of the activity, typically a price.
 * @property currencyCode Currency code for the amount, e.g., USD, EUR.
 * @property userRef Reference to the user associated with the itinerary, that is associated with
 * the activity.
 * @property itineraryRef Reference to the itinerary associated with the activity.
 *
 * @author ricardoceadev
 */

data class ActivityEntity(
    val id : String = "",
    val name : String = "",
    val latitude : Double = 0.0,
    val longitude : Double = 0.0,
    val activityDate : Instant,
    val description : String = "",
    val amount : String = "",
    val currencyCode : String = "",
    val userRef : EntityId? = null,
    val itineraryRef : EntityId? = null
)
