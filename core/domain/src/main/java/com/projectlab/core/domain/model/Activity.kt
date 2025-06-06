package com.projectlab.core.domain.model

/**
 * Represents an activity with its details.
 * This data class is used to encapsulate the information about an activity
 * inside the application, that comes from external services.
 *
 * @property id Unique identifier for the activity.
 * @property name Name of the activity.
 * @property description Description of the activity.
 * @property latitude Latitude coordinate of the activity location.
 * @property longitude Longitude coordinate of the activity location.
 * @property price Price of the activity.
 * @property currency Currency code for the price.
 * @property pictures List of picture URLs associated with the activity.
 * @property minimumDuration Minimum duration required for the activity.
 * @property rating Rating of the activity (out of 5).
 */

data class Activity(
    val id: String,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val price: String,
    val currency: String,
    val pictures: List<String>,
    val minimumDuration: String,
    val rating: Float
)