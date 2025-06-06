package com.projectlab.core.domain.repository

import com.projectlab.core.domain.entity.HotelEntity
import com.projectlab.core.domain.model.EntityId
import com.projectlab.core.domain.model.Hotel
import com.projectlab.core.domain.model.Response
import com.projectlab.core.domain.util.DataError
import com.projectlab.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * HotelsRepository interface for hotel-related operations.
 *
 * This interface defines the contract for hotel-related data operations,
 * including creating a hotel, retrieving hotels by various criteria,
 * updating and deleting hotels, and managing hotel favorites.
 *
 */

interface HotelsRepository {

    suspend fun createHotel(hotel: HotelEntity): kotlin.Result<EntityId>
    suspend fun getHotelById(userId: String, itinId: String, hotelId: String): kotlin.Result<HotelEntity?>
    suspend fun getAllHotels(userId: String, itinId: String): kotlin.Result<List<HotelEntity>>
    suspend fun updateHotel(hotel: HotelEntity): kotlin.Result<Unit>
    suspend fun deleteHotel(userId: String, itinId: String, hotelId: String): kotlin.Result<Unit>

    suspend fun getHotelsByCity(
        cityCode: String,
        amenities: String,
        ratings: String
    ): Result<List<Hotel>, DataError.Network>

    suspend fun favoriteHotel(userId: String, hotelId: String): Result<Boolean, DataError.Network>
    suspend fun unfavoriteHotel(userId: String, hotelId: String): Result<Boolean, DataError.Network>
}