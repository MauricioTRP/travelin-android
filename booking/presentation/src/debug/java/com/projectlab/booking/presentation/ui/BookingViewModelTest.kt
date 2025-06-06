package com.projectlab.booking.presentation.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.projectlab.core.domain.entity.ActivityEntity
import com.projectlab.core.domain.entity.FlightEntity
import com.projectlab.core.domain.entity.FlightSegmentEntity
import com.projectlab.core.domain.entity.HotelEntity
import com.projectlab.core.domain.entity.ItineraryEntity
import com.projectlab.core.domain.model.EntityId
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date
import android.util.Log
import com.projectlab.core.domain.model.User
import com.projectlab.core.domain.repository.ActivityRepository
import com.projectlab.core.domain.repository.FlightRepository
import com.projectlab.core.domain.repository.HotelsRepository
import com.projectlab.core.domain.repository.ItineraryRepository
import com.projectlab.core.domain.repository.UsersRepository
import java.util.UUID

/**
 * ViewModel for testing purposes, to create test data in Firestore.
 * This is not used in the app, but it is useful for testing purposes.
 * We should used in the debug build variant only.
 *
 *  @author ricardoceadev
 */

@HiltViewModel
class BookingViewModelTest @Inject constructor(
    private val userRepo: UsersRepository,
    private val itineraryRepo: ItineraryRepository,
    private val flightRepo: FlightRepository,
    private val hotelRepo: HotelsRepository,
    private val activityRepo: ActivityRepository,
    // TODO : add other repositories as needed: flight, hotel, etc.
) : ViewModel() {

    // we define a state in order to inform the UI about the data was successfully loaded or not
    private val _seedResult = MutableStateFlow<Result<Unit>?>(null)
    val seedResult = _seedResult.asStateFlow()
    // State for itineraries list:
    private val _itineraries = MutableStateFlow<List<ItineraryEntity>>(emptyList())
    val itineraries = _itineraries.asStateFlow()
    // State for one itinerary:
    private val _itinerary = MutableStateFlow<ItineraryEntity?>(null)
    val singleItinerary = _itinerary.asStateFlow()

    private val TAG = "BookingViewModelTest" // for logging purposes

    // hardcoding and persist data for Firestore testing purposes (through data layer)
    val randomUserId = UUID.randomUUID().toString() // Generate a random user ID for testing
    @RequiresApi(Build.VERSION_CODES.O)
    fun setupTestData() = viewModelScope.launch {
        // 1) We create a user and get the domain ID
        // Hardcode user data
        val user = User(
            id = randomUserId, // unique ID, could be generated or hardcoded for testing
            firstName = "SHEEV PALPATINE XX",
            lastName = "PALPATINE",
            countryCode = "66",
            phoneNumber = "66336633",
            email = "THESITH@gmail.com",
            age = "81"
        )

        // We create the user in Firestore through the repository
        val userIdRes = userRepo.createUser(user)
        if (userIdRes is com.projectlab.core.domain.model.Response.Failure) {
            val ex: Throwable = userIdRes.exception ?: RuntimeException(
                "Unknown error creating user."
            )
            _seedResult.value = Result.failure(ex) // indicate failure
            return@launch
        }

        // We get the user ID from the result
        val userId = EntityId(randomUserId)

        // 2) We create a Itinerary:
        val itinerary = ItineraryEntity(
            id                  = "",
            title = "Trip to the last empire...",
            startDate = Instant.now(),
            endDate = Instant.now().plusSeconds(2592000),
            totalItineraryPrice = 9000.0,
            userRef = userId
        )

        // We create the itinerary in Firestore through the repository
        val itinRes = itineraryRepo.createItinerary(itinerary)
        if (itinRes.isFailure) {
            _seedResult.value = Result.failure(itinRes.exceptionOrNull()!!)
            return@launch
        }

        // We get the itinerary ID from the result
        val itineraryId: EntityId = itinRes.getOrThrow()

        // 3) We create a flight:
        val flight = FlightEntity(
            airline = "quatar airways",
            flightNumber = "600",
            flightClass = "premium",
            departureAirport = mapOf(
                "airportCodeRef" to "CHI", // TODO: Add id form firestore
                "time" to Timestamp(Date(System.currentTimeMillis()))
            ),
            arrivalAirport = mapOf(
                "airportCodeRef" to "EUR", // TODO: Add id form firestore
                "time" to Timestamp(Date(System.currentTimeMillis()))
            ),
            passengerNumber = mapOf(
                "adultsNumber" to 2,
                "kidsNumber" to 0,
                "babiesWithSitNumber" to 3,
                "babiesInArmsNumber" to 1
            ),
            price = 4000.0,
            userRef = userId,
            itineraryRef = itineraryId
        )

        // We create the flight in Firestore through the repository
        val flightRes = flightRepo.createFlight(flight)
        if (flightRes.isFailure) {
            _seedResult.value = Result.failure(flightRes.exceptionOrNull()!!)
            return@launch
        }

        // We get the flight ID from the result
        val flightId: EntityId = flightRes.getOrThrow()

        // 4) We create a FlightSegment:
        val flightSegment = FlightSegmentEntity(
            departureAirportCodeRef = EntityId("MEX"), // TODO: Add id form firestore
            arrivalAirportCodeRef = EntityId("USA"), // TODO: Add id form firestore
            departureTime = Instant.now(),
            arrivalTime = Instant.now().plusSeconds(7200),
            requiresPlaneChange = false,
            connectionInfo = mapOf(
                "nextAirline" to "BRA",
                "nextFlightNumber" to "500",
                "connectionGate" to "A1"
            ),
            userRef = userId,
            itineraryRef = itineraryId,
            flightRef = flightId
        )

        // We create the flight segment in Firestore through the repository
        val flightSegmentRes = flightRepo.createFlightSegment(flightSegment)
        if (flightSegmentRes.isFailure) {
            _seedResult.value = Result.failure(flightSegmentRes.exceptionOrNull()!!)
            return@launch
        }

        // 5) We create a Hotel:
        val hotel = HotelEntity(
            hotelName = "Hotel of the Dark Side",
            hotelRoomNumber = 30,
            hotelPhone = 2222,
            locationRef = EntityId("location459"), // TODO: Add id form firestore
            guestName = "Darth Sidious",
            guestPhone = 1111111111,
            idNumber = 210,
            checkInDate = Instant.now(),
            checkOutDate = Instant.now().plusSeconds(86400),
            hotelPrice = 1500.0,
            userRef = userId,
            itineraryRef = itineraryId
        )

        // We create the hotel in Firestore through the repository
        val hotelRes = hotelRepo.createHotel(hotel)
        if (hotelRes.isFailure) {
            _seedResult.value = Result.failure(hotelRes.exceptionOrNull()!!)
            return@launch
        }

        // 6) We create an Activity:
        val activity = ActivityEntity(
            name = "Visit to the death star",
            latitude = 10.0,
            longitude = 400.0,
            activityDate = Instant.now(),
            description = "Discover the history of the empire and its most powerful weapon.",
            amount = "1000.0",
            currencyCode = "USD",
            userRef = userId,
            itineraryRef = itineraryId
        )

        // We create the activity in Firestore through the repository
        val activityRes = activityRepo.createActivity(activity)
        if (activityRes.isFailure) {
            _seedResult.value = Result.failure(activityRes.exceptionOrNull()!!)
            return@launch
        }

        // TODO: add other test data if it is needed

        _seedResult.value = Result.success(Unit) // indicate success, if all went well
    }
    
    // Launches the collection of all itineraries for a user:
    fun fetchAllItineraries(userId: String) = viewModelScope.launch {
        // We call to the repository to get all itineraries for the user
        val result = itineraryRepo.getAllItineraries(userId)

        if (result.isSuccess) {
            // If it was successful, we extract the list (it can be null, so we use ?: emptyList())
            val listOfItins: List<ItineraryEntity> = result.getOrThrow() ?: emptyList()
            _itineraries.value = listOfItins // update the state with the list of itineraries

            if (listOfItins.isEmpty()) {
                Log.d(TAG, "❌ No itineraries found for user: $userId")
            } else {
                Log.d(TAG, "✔ Fetched ${listOfItins.size} itineraries for user: $userId")
            }
        } else {
            // If the result is a failure, we log the error and update the state
            val ex = result.exceptionOrNull() ?: RuntimeException(
                "❓ Unknown error fetching itineraries."
            )
            _seedResult.value = Result.failure(ex) // indicate failure
            // log the error
            Log.e(TAG, "🚨 Error fetching itineraries for user: $userId", ex)
        }
    }

    // Launches the collection of a single itinerary by ID:
    fun fetchItineraryById(userId: String, itinId: String) = viewModelScope.launch {
        val result = itineraryRepo.getItineraryById(userId, itinId)

        if (result.isSuccess) {
            // It can be null if not exists
            val entity: ItineraryEntity? = result.getOrNull()
            _itinerary.value = entity

            if(entity == null) {
                Log.d(TAG, "❌ No itinerary found for user: $userId with ID: $itinId")
            } else {
                Log.d(TAG, "✔ Fetched itinerary for user: $userId with ID: $itinId")
            }
        }
        else {
            // If the result is a failure, we log the error and update the state
            val ex = result.exceptionOrNull() ?: RuntimeException(
                "❓ Unknown error fetching itinerary by ID."
            )
            _seedResult.value = Result.failure(ex) // indicate failure
            // log the error
            Log.e(TAG, "🚨 Error fetching itinerary for user: $userId with ID: $itinId", ex)
        }
    }
}