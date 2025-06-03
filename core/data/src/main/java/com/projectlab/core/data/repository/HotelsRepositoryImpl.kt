package com.projectlab.core.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.projectlab.core.data.mock.randomHotelAmenities
import com.projectlab.core.data.mock.randomHotelDisplayImageUrl
import com.projectlab.core.data.mock.randomHotelOffers
import com.projectlab.core.data.mock.randomHotelPhoneNumber
import com.projectlab.core.data.mock.randomHotelPhotoUrls
import com.projectlab.core.data.mock.randomHotelRating
import com.projectlab.core.data.remote.hotels.HotelsApiService
import com.projectlab.core.domain.model.Hotel
import com.projectlab.core.domain.model.HotelLocation
import com.projectlab.core.domain.repository.HotelsRepository
import com.projectlab.core.domain.util.DataError
import com.projectlab.core.domain.util.Result
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HotelsRepositoryImpl @Inject constructor(
    private val apiService: HotelsApiService,
    private val usersRef: CollectionReference
) : HotelsRepository {

    override suspend fun getHotelsByCity(
        cityCode: String,
        amenities: String,
        ratings: String
    ): Result<List<Hotel>, DataError.Network> {

        return try {
            val ratingQuery = "2, 3, 4, 5"
            val response =
                apiService.getHotelsByCity(cityCode, ratings = ratingQuery)

            val hotelsResponse = response.data.map { it ->
                val hotelOffers =
                    randomHotelOffers(stars = it.stars, countryCode = it.address.countryCode)
                val minHotelOffer = hotelOffers.minOf { it.price.amount }
                Hotel(
                    id = it.hotelId,
                    name = it.name,
                    location = HotelLocation(
                        longitude = it.geoCode.longitude,
                        latitude = it.geoCode.latitude,
                        country = it.address.countryCode,
                        city = it.iataCode,
                        // TODO GET LOCATION BY LON+LAT
                        address = "Plaza Parade, London NW6 5RP,",
                    ),
                    rating = randomHotelRating(it.stars),
                    displayPrice = minHotelOffer.toString(),
                    isFavourite = false,
                    displayImageUrl = randomHotelDisplayImageUrl(),
                    hotelOffers = hotelOffers,
                    amenities = randomHotelAmenities(),
                    photoUrls = randomHotelPhotoUrls(),
                    phoneNumber = randomHotelPhoneNumber()
                )
            }
            Result.Success(hotelsResponse)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun favoriteHotel(
        userId: String, hotelId: String
    ): Result<Boolean, DataError.Network> {
        return try {
            usersRef.document(userId).update("favoritesHotels", FieldValue.arrayUnion(hotelId))
                .await()
            Result.Success(true)

        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun unfavoriteHotel(
        userId: String,
        hotelId: String
    ): Result<Boolean, DataError.Network> {
        return try {
            usersRef.document(userId).update("favoritesHotels", FieldValue.arrayRemove(hotelId))
                .await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}