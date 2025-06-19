    package com.projectlab.booking.presentation.search.activities

    import com.projectlab.core.data.mapper.toDtoList
    import com.projectlab.core.data.remote.ActivitiesApiService
    import com.projectlab.core.data.usecase.GetActivitiesUseCase
    import com.projectlab.core.domain.model.Activity
    import com.projectlab.core.domain.model.Location
    import com.projectlab.core.domain.proto.SearchHistory
    import com.projectlab.core.domain.repository.SearchHistoryProvider
    import com.projectlab.core.domain.use_cases.location.GetCityFromCoordinatesUseCase
    import com.projectlab.core.domain.use_cases.location.GetCoordinatesFromCityUseCase
    import com.projectlab.core.domain.util.Result
    import com.projectlab.core.presentation.ui.utils.ErrorMapper
    import kotlinx.coroutines.ExperimentalCoroutinesApi
    import kotlinx.coroutines.test.runTest
    import kotlinx.coroutines.test.UnconfinedTestDispatcher
    import kotlinx.coroutines.test.setMain
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.test.advanceUntilIdle
    import org.junit.Before
    import org.junit.Test
    import org.mockito.kotlin.mock
    import org.mockito.kotlin.whenever
    import org.junit.Assert.assertEquals
    import org.junit.Assert.assertFalse
    import org.junit.Assert.assertNull


    @OptIn(ExperimentalCoroutinesApi::class)
    class SearchActivityViewModelTest {

        private lateinit var viewModel: SearchActivityViewModel
        private lateinit var activitiesApiService: ActivitiesApiService
        private lateinit var getActivitiesUseCase: GetActivitiesUseCase
        private lateinit var getCoordinatesFromCityUseCase: GetCoordinatesFromCityUseCase
        private lateinit var getCityFromCoordinatesUseCase: GetCityFromCoordinatesUseCase
        private lateinit var searchHistoryProvider: SearchHistoryProvider
        private lateinit var errorMapper: ErrorMapper

        @Before
        fun setup() = runTest {
            Dispatchers.setMain(UnconfinedTestDispatcher())

            activitiesApiService = mock()
            getActivitiesUseCase = mock()
            getCoordinatesFromCityUseCase = mock()
            getCityFromCoordinatesUseCase = mock()
            searchHistoryProvider = mock()
            errorMapper = mock()

            whenever(searchHistoryProvider.getSearchHistory(SearchHistory.HistoryType.ACTIVITY)).thenReturn(
                emptyList()
            )

            viewModel = SearchActivityViewModel(
                activitiesApiService,
                getActivitiesUseCase,
                getCoordinatesFromCityUseCase,
                getCityFromCoordinatesUseCase,
                errorMapper,
                searchHistoryProvider
            )

        }

        @Test
        fun `should return activities when search by city succeeds`() = runTest {
            val fakeQuery = "Santiago"
            val fakeCoordinates = Pair(-33.4489, -70.6693)

            val fakeActivities = listOf(
                Activity(
                    id = "1",
                    name = "Tour por la ciudad",
                    description = "Recorre los principales lugares turísticos.",
                    latitude = -33.4489,
                    longitude = -70.6693,
                    price = "15000",
                    currency = "CLP",
                    pictures = listOf("https://example.com/tour.jpg"),
                    minimumDuration = "2h",
                    rating = 4.5f
                ),
                Activity(
                    id = "2",
                    name = "Caminata por el cerro",
                    description = "Actividad física y conexión con la naturaleza.",
                    latitude = -33.4500,
                    longitude = -70.6600,
                    price = "10000",
                    currency = "CLP",
                    pictures = listOf("https://example.com/cerro.jpg"),
                    minimumDuration = "1.5h",
                    rating = 4.2f
                )
            )

            // Mocks
            whenever(searchHistoryProvider.getSearchHistory(SearchHistory.HistoryType.ACTIVITY))
                .thenReturn(emptyList())

            whenever(searchHistoryProvider.addSearchEntry(SearchHistory.HistoryType.ACTIVITY, fakeQuery))
                .thenReturn(listOf())

            whenever(getCoordinatesFromCityUseCase(fakeQuery))
                .thenReturn(fakeCoordinates)

            whenever(getActivitiesUseCase(fakeCoordinates.first, fakeCoordinates.second))
                .thenReturn(Result.Success(fakeActivities))

            viewModel = SearchActivityViewModel(
                activitiesApiService,
                getActivitiesUseCase,
                getCoordinatesFromCityUseCase,
                getCityFromCoordinatesUseCase,
                errorMapper,
                searchHistoryProvider
            )

            viewModel.searchWithInitialQuery(fakeQuery)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(2, state.activities.size)

            val expectedList = fakeActivities.toDtoList()

            expectedList.forEachIndexed { index, expected ->
                val actual = state.activities[index]
                assertEquals(expected.id, actual.id)
                assertEquals(expected.name, actual.name)
                assertEquals(expected.description, actual.description)
                assertEquals(expected.price, actual.price)
                assertEquals(expected.pictures, actual.pictures)
                assertEquals(expected.minimumDuration, actual.minimumDuration)
                assertEquals(expected.rating, actual.rating, 0.01f)
            }

            assertFalse(state.isLoading)
            assertNull(state.error)
        }

        @Test
        fun `should return activities when searching by current location`() = runTest {
            val fakeCoordinates = Pair(-33.4489, -70.6693)
            val fakeLocation = Location(latitude = fakeCoordinates.first, longitude = fakeCoordinates.second)
            val fakeCity = "Santiago"

            val fakeActivities = listOf(
                Activity(
                    id = "1",
                    name = "Tour por la ciudad",
                    description = "Recorre lugares turísticos",
                    latitude = -33.4489,
                    longitude = -70.6693,
                    price = "15000",
                    currency = "CLP",
                    pictures = listOf("https://example.com/tour.jpg"),
                    minimumDuration = "2h",
                    rating = 4.5f
                )
            )

            whenever(searchHistoryProvider.getSearchHistory(SearchHistory.HistoryType.ACTIVITY)).thenReturn(emptyList())
            whenever(getCityFromCoordinatesUseCase(fakeCoordinates.first, fakeCoordinates.second)).thenReturn(fakeCity)
            whenever(getActivitiesUseCase(fakeCoordinates.first, fakeCoordinates.second)).thenReturn(Result.Success(fakeActivities))


            viewModel.searchByCurrentLocation(fakeLocation)
            advanceUntilIdle()

            val state = viewModel.uiState.value

            val expected = fakeActivities.toDtoList()
            assertEquals(1, state.activities.size)
            assertEquals(expected[0].id, state.activities[0].id)
            assertEquals(fakeCity, state.address)
            assertFalse(state.isLoading)
            assertNull(state.error)
        }

    }
