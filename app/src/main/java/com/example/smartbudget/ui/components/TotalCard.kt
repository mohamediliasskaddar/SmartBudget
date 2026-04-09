// ui/components/TotalCard.kt
package com.example.smartbudget.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartbudget.ui.theme.*
import com.example.smartbudget.util.CurrencyUtils

@Composable
fun TotalCard(
    total: Double,
    modifier: Modifier = Modifier,
    currency: String = "MAD",
    label: String = "Total du mois"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(indigoPrimary, blueAccent)
                )
            )
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        // Cercle décoratif haut droite (echo du bg)
        Box(
            modifier = Modifier
                .size(90.dp)
                .align(Alignment.TopEnd)
                .offset(x = 20.dp, y = (-20).dp)
                .background(
                    color = white.copy(alpha = 0.07f),
                    shape = RoundedCornerShape(50)
                )
        )

        Column {
            Text(
                text       = label,
                fontSize   = 13.sp,
                color      = textOnDarkSub,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text       = CurrencyUtils.format(total, currency),
                fontSize   = 32.sp,
                fontWeight = FontWeight.Bold,
                color      = white
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text     = "Ce mois en cours",
                fontSize = 12.sp,
                color    = textOnDarkSub
            )
        }
    }
}