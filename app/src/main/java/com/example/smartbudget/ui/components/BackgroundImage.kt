// ui/components/BackgroundImage.kt
package com.example.smartbudget.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.smartbudget.R
import com.example.smartbudget.ui.theme.skyBlue

@Composable
fun BackgroundImage(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Image de fond
        Image(
            painter            = painterResource(id = R.drawable.bg1),
            contentDescription = null,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier.fillMaxSize()
        )

        // Fondu bas → sky blue pour transition douce vers le contenu scrollable
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, skyBlue.copy(alpha = 0.18f))
                    )
                )
        )

        // Contenu
        Box(
            modifier           = Modifier.fillMaxSize(),
            contentAlignment   = Alignment.TopStart
        ) {
            content()
        }
    }
}