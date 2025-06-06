package com.projectlab.core.data.model

import com.projectlab.core.domain.model.Activity

/**
 * Data Transfer Object (DTO) representing an activity.
 * This is used to transfer data between the application and the API.
 * This is the model that is used for the API communication.
 *
 * @property id Unique identifier for the activity.
 * @property name Name of the activity.
 * @property description Description of the activity.
 * @property geoCode Location data of the activity.
 * @property price Price information of the activity.
 * @property pictures List of picture URLs associated with the activity.
 * @property minimumDuration Minimum duration required for the activity.
 * @property rating Rating of the activity.
 */

data class ActivityDto(
    val id: String,
    val name: String,
    val description: String,
    val geoCode: LocationDataDto,
    val price: PriceDto,
    val pictures: List<String>,
    val minimumDuration: String,
    val rating: Float
)

/*fun Activity.toDto(): ActivityDto {
    return ActivityDto(
        id = id,
        name = name,
        description = description,
        geoCode = LocationDataDto(latitude, longitude),
        price = PriceDto(price, currency),
        pictures = pictures,
        minimumDuration = minimumDuration,
        rating = rating
    )
}*/
