package com.projectlab.booking.presentation.screens.hotels.details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.projectlab.booking.presentation.HotelsViewModel
import com.projectlab.booking.presentation.screens.hotels.details.components.DetailHotelBottomBar
import com.projectlab.booking.presentation.screens.hotels.details.components.DetailHotelContent
import com.projectlab.core.presentation.designsystem.component.HeaderWithBack

@Composable
fun DetailHotelScreen(
    modifier: Modifier = Modifier,
    viewModel: HotelsViewModel,
    hotelId: String,
    onClickBack: () -> Unit,
    onClickBookingHotel: () -> Unit
) {
    val state by viewModel.uiStateHotelDetails.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getHotelDetails(hotelId)
    }

    if (state.hotelUi != null) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { HeaderWithBack(onClickBack = onClickBack) },
            content = {
                DetailHotelContent(
                    modifier = modifier.padding(it),
                    hotelUi = state.hotelUi!!
                )
            },
            bottomBar = {
                DetailHotelBottomBar(
                    hotelUi = state.hotelUi!!,
                    onClickBookingHotel = { onClickBookingHotel() }
                )
            }
        )
    }
}