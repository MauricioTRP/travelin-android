package com.projectlab.booking.presentation.search.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectlab.core.data.mapper.toDtoList
import com.projectlab.core.data.model.ActivityDto
import com.projectlab.core.domain.entity.FavoriteActivityEntity
import com.projectlab.core.domain.model.Location
import com.projectlab.core.domain.proto.SearchHistory.HistoryType
import com.projectlab.core.domain.repository.ActivityRepository
import com.projectlab.core.domain.repository.SearchHistoryProvider
import com.projectlab.core.domain.use_cases.activities.GetActivitiesUseCase
import com.projectlab.core.domain.use_cases.activities.RemoveFavoriteActivityByIdUseCase
import com.projectlab.core.domain.use_cases.activities.SaveFavoriteActivityUseCase
import com.projectlab.core.domain.use_cases.error.ErrorMapper
import com.projectlab.core.domain.util.Result
import com.projectlab.core.domain.use_cases.location.GetCityFromCoordinatesUseCase
import com.projectlab.core.domain.use_cases.location.GetCoordinatesFromCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the SearchActivity screen.
 * It handles the logic for searching activities based on user input and location.
 *
 * @param getActivitiesUseCase Use case for getting activities.
 * @param errorMapper Mapper for converting errors to user-friendly messages.
 */

@HiltViewModel
class SearchActivityViewModel @Inject constructor(
    private val getActivitiesUseCase: GetActivitiesUseCase,
    private val getCoordinatesFromCityUseCase: GetCoordinatesFromCityUseCase,
    private val getCityFromCoordinatesUseCase: GetCityFromCoordinatesUseCase,
    private val saveFavoriteActivityUseCase: SaveFavoriteActivityUseCase,
    private val removeFavoriteActivityByIdUseCase: RemoveFavoriteActivityByIdUseCase,
    private val activityRepository: ActivityRepository,
    private val errorMapper: ErrorMapper,
    private val historyProvider: SearchHistoryProvider,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchActivityUiState())
    val uiState: StateFlow<SearchActivityUiState> = _uiState.asStateFlow()

    private val _favoriteActivityIds = MutableStateFlow<List<String>>(emptyList())
    val favoriteActivityIds: StateFlow<List<String>> = _favoriteActivityIds.asStateFlow()

    private val favoriteIdsSet: MutableSet<String> = mutableSetOf()

    init {
        // Load search history when the ViewModel is created
        viewModelScope.launch {
            val list = historyProvider.getSearchHistory(HistoryType.ACTIVITY)
            _uiState.update { it.copy(history = list.orEmpty().reversed()) }
        }
    }

    fun onQueryChanged(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
    }

    /**
     * onSearchSubmitted() is called when the user submits a search query.
     * It fetches activities based on the provided location.
     * It updates the UI state with the results or an error message.
     */

    fun searchWithInitialQuery(query: String) {
        val currentState = uiState.value

        if (currentState.searchOrigin == SearchOrigin.LOCATION) return

        if (query == currentState.query && currentState.activities.isNotEmpty()) return

        _uiState.update {
            it.copy(
                query = query,
                searchOrigin = SearchOrigin.QUERY
            )
        }

        onSearchSubmitted()
    }

    fun onSearchSubmitted() {
        onSearchSubmitted(false)
    }

    fun searchByCurrentLocation(location: Location) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val addressString = getCityFromCoordinatesUseCase(
                    location.latitude,
                    location.longitude,
                )
                _uiState.update { it.copy(address = addressString, query = addressString) }


                when (val result = getActivitiesUseCase(location.latitude, location.longitude)) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                activities = result.data.toDtoList(),
                                searchOrigin = SearchOrigin.LOCATION
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(error = errorMapper.map(result.error)) }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage ?: "Unknown error") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun showAllResults() {
        _uiState.update { it.copy(showAllResults = true) }
    }

    fun onDeleteHistoryEntry(value: String) {
        viewModelScope.launch {
            val updated = historyProvider.removeSearchEntry(HistoryType.ACTIVITY, value)
            _uiState.update { it.copy(history = updated.reversed()) }
        }
    }

    fun setFavorite(activity: ActivityDto, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                if (!isFavorite) {
                    removeFavoriteActivityByIdUseCase(activity.id)
                    return@launch
                }

                val location = getCityFromCoordinatesUseCase(
                    activity.geoCode.latitude,
                    activity.geoCode.longitude,
                )

                val favoriteActivity = FavoriteActivityEntity(
                    id = activity.id,
                    name = activity.name,
                    description = activity.description,
                    minimumDuration = activity.minimumDuration,
                    price = activity.price.amount,
                    currency = activity.price.currencyCode,
                    rating = activity.rating,
                    location = location,
                    latitude = activity.geoCode.latitude,
                    longitude = activity.geoCode.longitude,
                    pictures = activity.pictures,
                )

                saveFavoriteActivityUseCase(favoriteActivity)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage ?: "Unknown error") }
            }
        }
    }

    private fun onSearchSubmitted(isInitial: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                if (!isInitial) {
                    val updated = historyProvider.addSearchEntry(
                        type = HistoryType.ACTIVITY,
                        value = uiState.value.query,
                    )
                    _uiState.update { it.copy(history = updated.reversed()) }
                }

                val locationData = getCoordinatesFromCityUseCase(uiState.value.query)
                if (locationData != null) {
                    when (val result = getActivitiesUseCase(
                        locationData.first,
                        locationData.second,
                    )) {
                        is Result.Success -> {
                            _uiState.update { it.copy(activities = result.data.toDtoList()) }
                        }

                        is Result.Error -> {
                            _uiState.update { it.copy(error = errorMapper.map(result.error)) }
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(error = "Could not find coordinates for the specified city")
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage ?: "Unknown error") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun fetchFavoriteActivities() {
        viewModelScope.launch {
            activityRepository.getFavoriteActivities().collect { favorites ->
                _uiState.update { it.copy(favoriteActivities = favorites) }
            }
        }
    }

    fun toggleFavoriteActivity(activity: FavoriteActivityEntity) {
        _uiState.update { it.copy(isFavoriteLoading = true) }
        viewModelScope.launch {
            try {
                if (favoriteIdsSet.contains(activity.id)) {
                    removeFavoriteActivityByIdUseCase(activity.id)
                    favoriteIdsSet.remove(activity.id)
                } else {
                    val result = saveFavoriteActivityUseCase(activity)
                    if (result.isFailure) {
                        throw result.exceptionOrNull() ?: Exception("Unknown error saving favorite")
                    }
                    favoriteIdsSet.add(activity.id)
                }
                _favoriteActivityIds.value = favoriteIdsSet.toList()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage ?: "Unknown error") }
            } finally {
                _uiState.update { it.copy(isFavoriteLoading = false) }
            }
        }
    }
}
