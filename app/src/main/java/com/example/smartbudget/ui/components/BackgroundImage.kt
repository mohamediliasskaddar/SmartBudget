// ui/components/BackgroundImage.kt
package com.example.smartbudget.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.smartbudget.R

/**
 * Background image layer with adaptive dark/light overlay for accessibility
 * @param modifier Modifier to apply to the background container
 * @param overlayOpacity Opacity of the overlay (0f-1f). Higher = darker overlay (better readability)
 * @param content Composable content to overlay on the background
 */
@Composable
fun BackgroundImage(
    modifier: Modifier = Modifier,
    overlayOpacity: Float? = null,
    content: @Composable () -> Unit
) {
    val isDarkMode = isSystemInDarkTheme()
    // Auto-adjust overlay opacity based on theme for better readability
    val finalOpacity = overlayOpacity ?: if (isDarkMode) 0.5f else 0.35f

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.bg1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Content on top
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            content()
        }
    }
}

