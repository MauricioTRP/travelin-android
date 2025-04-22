package com.projectlab.travelin_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.projectlab.core.presentation.designsystem.BadgeInfo
import com.projectlab.core.presentation.designsystem.BadgeOutline
import com.projectlab.core.presentation.designsystem.BadgePriceUnit
import com.projectlab.core.presentation.designsystem.ButtonOutline
import com.projectlab.core.presentation.designsystem.ButtonPrimary
import com.projectlab.core.presentation.designsystem.ButtonSecondary
import com.projectlab.core.presentation.designsystem.ButtonTertiary
import com.projectlab.core.presentation.designsystem.GradientBackground
import com.projectlab.core.presentation.designsystem.theme.TravelinTheme
import com.projectlab.core.presentation.desingsystem.component.IconButtonHeart
import com.projectlab.core.presentation.desingsystem.component.ButtonHotel
import com.projectlab.core.presentation.desingsystem.component.ButtonOversea
import com.projectlab.core.presentation.designsystem.theme.TravelinTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelinTheme(dynamicColor = true) {
                Scaffold(
                    content = { padding ->
                        Column(Modifier
                            .padding(padding)
                            .padding(horizontal = 16.dp)) {
                            ExampleUI()
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun ExampleUI(modifier: Modifier = Modifier) {
    var checked by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.fillMaxSize()
    )
    { IconButtonHeart(
        onCheckedChange =  {
            checked = it
        },
        modifier = modifier,
        checked = checked
    )

        ButtonHotel(modifier = modifier, onClick = {})
        ButtonOversea(modifier = modifier, onClick = {})
    }


@Preview(showBackground = true)
@Composable
fun GradientPreview() {
    TravelinTheme(dynamicColor = false, darkTheme = false) {
        GradientBackground {
            Text("¡Hola, quiuboles!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonPrimaryPreview() {
    TravelinTheme(dynamicColor = false, darkTheme = false) {
        ButtonPrimary(
            text = "Primary Button",
            onClick = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ButtonSecondaryPreview() {
    TravelinTheme(dynamicColor = false, darkTheme = false) {
        ButtonSecondary(
            text = "Secondary Button",
            onClick = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ButtonTertiaryPreview() {
    TravelinTheme(dynamicColor = false, darkTheme = false) {
        ButtonTertiary(
            text = "Tertiary Button",
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonOutlinePreview() {
    TravelinTheme(dynamicColor = false, darkTheme = false) {
        ButtonOutline(
            text = "Outline Button",
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BadgeOutlinePreview() {
    TravelinTheme(dynamicColor = false, darkTheme = false) {
        BadgeOutline("2 day 1 night")
    }
}

@Preview(showBackground = true)
@Composable
fun BadgePriceUnitPreview() {
    TravelinTheme(dynamicColor = false, darkTheme = false) {
        BadgePriceUnit("$200", "2 person")
    }
}

@Preview(showBackground = true)
@Composable
fun BadgeInfoPreview() {
    TravelinTheme(dynamicColor = false, darkTheme = false) {
        BadgeInfo(title = "2 day 1 night", subtitle = "Duration", icon = Icons.Filled.Place)
    }
}