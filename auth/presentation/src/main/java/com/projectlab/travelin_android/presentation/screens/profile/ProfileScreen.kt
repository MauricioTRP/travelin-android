package com.projectlab.travelin_android.presentation.screens.profile

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.projectlab.core.presentation.designsystem.component.BottomNavigationBar
import com.projectlab.travelin_android.presentation.screens.profile.components.ProfileContent

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogoutClick: () -> Unit,
    onHomeClick: () -> Unit

) {
    Scaffold(
        topBar = { },
        content = {
            ProfileContent(
                paddingValues = it,
                viewModel = viewModel,
                onLogoutClick = onLogoutClick
            )
        },
        bottomBar = { BottomNavigationBar("profile", onHomeClick) }
    )
}