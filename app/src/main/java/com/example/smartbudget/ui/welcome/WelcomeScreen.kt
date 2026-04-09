package com.example.smartbudget.ui.welcome

import android.R.attr.bottom
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.smartbudget.R
import com.example.smartbudget.ui.navigation.Screen
import com.example.smartbudget.ui.theme.SmartBudgetTheme
import com.example.smartbudget.ui.theme.skyBlue
import com.example.smartbudget.ui.theme.blueAccent

@Composable
fun WelcomeScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {

        // 🖼️ Background image
        Image(
            painter = painterResource(id = R.drawable.welcome),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 🌑 Overlay sombre (important pour lisibilité)
       //style :  clean UI to match Figma templates
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Black.copy(alpha = 0.4f))
//        )

        // 🔘 Contenu (texte + bouton)
        Column(
//            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),

            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
//                text = "It isn’t what you earn but how you spend it that fixes your class",
                text = "Ce n’est pas ce que vous gagnez, mais la façon dont vous le dépensez qui détermine votre classe",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                lineHeight = 36.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 120.dp)
                    .clip(RoundedCornerShape(16.dp))
//                    .background(blueAccent)
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)                 
            )

            // 🔘 Button
            Button(
                onClick = {
                    navController.navigate(Screen.Expenses.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .heightIn(min = 48.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = skyBlue,
                    contentColor = White
                )
            ) {
                Text("Commencer")
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    SmartBudgetTheme {
        WelcomeScreen(navController = rememberNavController())
    }
}