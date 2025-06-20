package com.projectlab.booking.presentation.screens.hotels.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.projectlab.booking.models.HotelUi
import com.projectlab.booking.presentation.R
import com.projectlab.core.presentation.designsystem.component.ButtonComponent
import com.projectlab.core.presentation.designsystem.component.ButtonVariant
import com.projectlab.core.presentation.designsystem.theme.spacing

@Composable
fun DetailHotelBottomBar(
    modifier: Modifier = Modifier,
    hotelUi: HotelUi,
    onClickBookingHotel: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.spacing.ImageSize)
            .padding(
                horizontal = MaterialTheme.spacing.ScreenHorizontalPadding,
                vertical = MaterialTheme.spacing.ScreenVerticalSpacing
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.price_per_night, hotelUi.price.formatted),
            fontSize = 18.sp,
            fontWeight = FontWeight.W600,
            color = MaterialTheme.colorScheme.primary
        )
        ButtonComponent(
            text = stringResource(R.string.book_now),
            onClick = { onClickBookingHotel() },
            variant = ButtonVariant.Primary,
        )
    }
}