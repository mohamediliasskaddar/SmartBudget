// ui/settings/SettingsScreen.kt
package com.example.smartbudget.ui.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartbudget.SmartBudgetApp
import com.example.smartbudget.ui.components.BackgroundImage
import com.example.smartbudget.ui.theme.*

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val app     = context.applicationContext as SmartBudgetApp
    val vm: SettingsViewModel = viewModel(
        factory = SettingsViewModel.Factory(app.categoryRepository, app.expenseRepository)
    )

    val categories by vm.categories.collectAsState()
    val uiState    by vm.uiState.collectAsState()

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { vm.importFromUri(context, it) } }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.exportMessage, uiState.importMessage) {
        val msg = uiState.exportMessage ?: uiState.importMessage
        if (msg != null) { snackbarHostState.showSnackbar(msg); vm.clearMessages() }
    }

    BackgroundImage(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            snackbarHost   = { SnackbarHost(snackbarHostState) }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // ── HEADER ─────────────────────────────
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 16.dp, bottom = 24.dp)
                ) {
                    Text(
                        text       = "Paramètres",
                        fontSize   = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color      = white
                    )
                    Text(
                        text     = "Gestion des catégories et données",
                        fontSize = 13.sp,
                        color    = textOnDarkSub
                    )
                }

                // ── CONTENT ────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(
                            Brush.verticalGradient(colors = listOf(surfaceCard, white))
                        )
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top   = 24.dp,
                            bottom = 100.dp // 👈 espace pour navbar/FAB
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // ── Données
                        item {
                            Text(
                                "Données",
                                fontSize   = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color      = textSecondary
                            )
                        }

                        item {
                            Card(
                                shape     = RoundedCornerShape(16.dp),
                                colors    = CardDefaults.cardColors(containerColor = surfaceCard),
                                elevation = CardDefaults.cardElevation(0.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick  = { vm.exportCurrentMonth(context) },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape    = RoundedCornerShape(12.dp),
                                        colors   = ButtonDefaults.buttonColors(
                                            containerColor = indigoPrimary,
                                            contentColor   = white
                                        )
                                    ) {
                                        Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(17.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text("Exporter le mois (CSV)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                    }

                                    OutlinedButton(
                                        onClick  = { importLauncher.launch("text/*") },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape    = RoundedCornerShape(12.dp),
                                        colors   = ButtonDefaults.outlinedButtonColors(contentColor = indigoPrimary),
                                        border   = androidx.compose.foundation.BorderStroke(1.dp, indigoPrimary.copy(alpha = 0.4f))
                                    ) {
                                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(17.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text("Importer un CSV", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                    }
                                }
                            }
                        }

                        // ── Catégories
                        item {
                            Text(
                                "Catégories",
                                fontSize   = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color      = textSecondary
                            )
                        }

                        items(categories) { cat ->
                            Card(
                                shape     = RoundedCornerShape(14.dp),
                                colors    = CardDefaults.cardColors(containerColor = surfaceCard),
                                elevation = CardDefaults.cardElevation(0.dp),
                                modifier  = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment     = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment     = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(
                                                    if (cat.isActive) indigoPrimary.copy(alpha = 0.10f)
                                                    else textSecondary.copy(alpha = 0.07f)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(cat.icon, fontSize = 18.sp)
                                        }

                                        Column {
                                            Text(
                                                cat.name,
                                                fontSize   = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                color      = if (cat.isActive) textPrimary else textSecondary
                                            )
                                            Text(
                                                if (cat.isActive) "Active" else "Désactivée",
                                                fontSize = 12.sp,
                                                color    = if (cat.isActive) successGreen else textSecondary.copy(alpha = 0.5f)
                                            )
                                        }
                                    }

                                    Switch(
                                        checked         = cat.isActive,
                                        onCheckedChange = { vm.toggleCategory(cat) },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor   = white,
                                            checkedTrackColor   = indigoPrimary,
                                            uncheckedThumbColor = white,
                                            uncheckedTrackColor = textSecondary.copy(alpha = 0.25f)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}