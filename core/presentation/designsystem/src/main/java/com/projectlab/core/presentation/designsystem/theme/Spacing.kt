package com.projectlab.core.presentation.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
object Spacing {
    /** 0.dp */
    val none: Dp = 0.dp

    /** 2.dp */
    val tiny: Dp = 2.dp

    /** 4.dp */
    val extraSmall: Dp = 4.dp

    /** 8.dp */
    val small: Dp = 8.dp

    /** 10.dp */
    val regular: Dp = 10.dp

    /** 16.dp */
    val medium: Dp = 16.dp

    /** 24.dp */
    val semiLarge: Dp = 24.dp

    /** 32.dp */
    val large: Dp = 32.dp

    /** 40.dp */
    val extraLarge: Dp = 40.dp

    /** 48.dp */
    val extraLarge2: Dp = 48.dp

    /** 56.dp */
    val semiHuge: Dp = 56.dp

    /** 64.dp */
    val huge: Dp = 64.dp

    // Global styles
    val FieldHeight: Dp = 52.dp

    // Auth Layout spacing
    val ScreenHorizontalPadding: Dp = 30.dp
    val ScreenVerticalSpacing: Dp = 14.dp
    val SectionSpacing: Dp = 20.dp
    val ElementSpacing: Dp = 12.dp
    val BigSpacing: Dp = 70.dp
    val SmallSpacing: Dp = 8.dp
    val TinySpacing: Dp = 4.dp
    val Spacer: Dp = 1.dp

    // Auth Component sizing
    val ButtonHeight: Dp = 65.dp
    val BottomBarHeight: Dp = 69.dp
    val IconSize: Dp = 24.dp
    val SmallIconSize: Dp = 20.dp
    val ImageSize: Dp = 80.dp
    val LoginLogoWidth: Dp = 103.dp
    val LoginLogoHeight: Dp = 102.dp
    val RegisteredLogoWidth: Dp = 137.dp
    val RegisteredLogoHeight: Dp = 139.dp

    // Auth Container styles
    val CornerRadius: Dp = 20.dp
    val SmallCornerRadius: Dp = 12.dp

    // Profile setting button
    val ProfileButtonHeight: Dp = 62.dp
    const val ProfileButtonArrowScale: Float = 1.25f

    // Search screen
    val searchPlacesPadding: Dp = 6.dp
    val searchSpacer: Dp = 10.dp

    // Gallery container size
    val galleryImageSize: Dp = 200.dp

    // Header sizing
    val homeHeaderImageSize: Dp = 400.dp
    val TourCardHeaderSize: Dp = 428.dp
    val homeHeaderSpacer: Dp = 105.dp
    val searchBarWidth: Dp = 331.dp

    // Vertical Favorite Card
    val favoriteCardHeight: Dp = 300.dp
    val favoriteImageHeight: Dp = 200.dp
    val favoriteButtonSize: Dp = 40.dp
    val favoriteButtonPadding: Dp = 16.dp

    // Home
    val recommendedImageHeight: Dp = 236.dp
    val recommendedCardWidth: Dp = 230.dp
    val recommendedSectionHeight: Dp = 700.dp
    val homeHeight: Dp = 1300.dp

    val mapHeight: Dp = 200.dp
  
    // Booking
    val BookingLogoWidth: Dp = 137.dp
    val BookingLogoHeight: Dp = 139.dp

}

val LocalSpacing = staticCompositionLocalOf { Spacing }

val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current
