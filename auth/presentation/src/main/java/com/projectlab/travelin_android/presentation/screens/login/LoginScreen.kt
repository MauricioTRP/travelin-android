package com.projectlab.travelin_android.presentation.screens.login

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.projectlab.core.domain.model.Response
import com.projectlab.travelin_android.presentation.screens.login.components.LoginBottomBar
import com.projectlab.travelin_android.presentation.screens.login.components.LoginContent

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onRegisterClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Scaffold(
        content = {
            LoginContent(
                paddingValues = it,
                viewModel = viewModel,
            )
        },
        bottomBar = { LoginBottomBar(onRegisterClick = onRegisterClick) }
    )

    val loginFlow = viewModel.loginFlow.collectAsState()

    loginFlow.value.let {
        when (it) {
            Response.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            is Response.Failure -> {
                Toast.makeText(
                    LocalContext.current, "User fail ${it.exception?.message} ",
                    Toast.LENGTH_LONG
                ).show()
            }

            is Response.Success -> {
                LaunchedEffect(Unit) {
                    onProfileClick()
                }
                Toast.makeText(
                    LocalContext.current, "User logged ",
                    Toast.LENGTH_LONG
                ).show()
            }

            null -> {}
        }
    }
}