package com.projectlab.travelin_android.presentation.screens.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.projectlab.core.presentation.designsystem.theme.spacing
import com.projectlab.travelin_android.presentation.components.ButtonSimple
import com.projectlab.travelin_android.presentation.components.OutlinedButtonWithIcons
import com.projectlab.travelin_android.presentation.screens.profile.ProfileViewModel

@Composable
fun ProfileSettings(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.ScreenHorizontalPadding)
    ) {
        Text(
            text = "Account Setting",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.SectionSpacing))

        OutlinedButtonWithIcons(
            icon = Icons.Default.AccountCircle,
            title = "Edit profile",
            onClick = { }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.ElementSpacing))

        OutlinedButtonWithIcons(
            icon = Icons.Default.DarkMode,
            title = "Color mode",
            onClick = { }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.BigSpacing))

        ButtonSimple(
            text = "Logout",
            onClick = {
                viewModel.logout()
                onLogoutClick()
            },
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            isOutlined = true
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.ElementSpacing))

        Text(
            text = "Version 3.0.0",
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
