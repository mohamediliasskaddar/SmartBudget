// ui/components/MonthNavigator.kt
package com.example.smartbudget.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartbudget.ui.theme.*
import java.text.DateFormatSymbols

@Composable
fun MonthNavigator(
    year: Int,
    month: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val monthName = DateFormatSymbols.getInstance().months[month - 1]
        .replaceFirstChar { it.uppercase() }

    Row(
        modifier              = modifier.fillMaxWidth(),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Bouton précédent — cercle glassmorphism
        Surface(
            shape  = CircleShape,
            color  = surfaceGlass,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        ) {
            IconButton(onClick = onPrevious) {
                Icon(
                    imageVector        = Icons.Default.ChevronLeft,
                    contentDescription = "Mois précédent",
                    tint               = white
                )
            }
        }

        // Mois + année
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text       = monthName,
                fontSize   = 20.sp,
                fontWeight = FontWeight.Bold,
                color      = white
            )
            Text(
                text       = year.toString(),
                fontSize   = 13.sp,
                fontWeight = FontWeight.Normal,
                color      = textOnDarkSub
            )
        }

        // Bouton suivant
        Surface(
            shape    = CircleShape,
            color    = surfaceGlass,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        ) {
            IconButton(onClick = onNext) {
                Icon(
                    imageVector        = Icons.Default.ChevronRight,
                    contentDescription = "Mois suivant",
                    tint               = white
                )
            }
        }
    }
}