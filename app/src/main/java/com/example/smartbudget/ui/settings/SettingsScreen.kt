// ui/settings/SettingsScreen.kt
package com.example.smartbudget.ui.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartbudget.SmartBudgetApp
import com.example.smartbudget.ui.components.BackgroundImage
import com.example.smartbudget.ui.theme.indigoPrimary
import com.example.smartbudget.ui.theme.white

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val app     = context.applicationContext as SmartBudgetApp
    val vm: SettingsViewModel = viewModel(
        factory = SettingsViewModel.Factory(app.categoryRepository, app.expenseRepository)
    )

    val categories by vm.categories.collectAsState()
    val uiState    by vm.uiState.collectAsState()

    // Picker fichier CSV pour l'import
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { vm.importFromUri(context, it) }
    }

    // Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.exportMessage, uiState.importMessage) {
        val msg = uiState.exportMessage ?: uiState.importMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            vm.clearMessages()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        BackgroundImage {
            LazyColumn(
                modifier            = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding      = PaddingValues(vertical = 16.dp)
            ) {

            // ── Section Export / Import ─────────────────────────
            item {
                Text("Données", style = MaterialTheme.typography.titleMedium)
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Export
                        Button(
                            onClick  = { vm.exportCurrentMonth(context) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = indigoPrimary,
                                contentColor = white
                            )
                        ) {
                            Icon(Icons.Default.Upload,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Exporter le mois en cours (CSV)")
                        }

                        // Import
                        OutlinedButton(
                            onClick  = { importLauncher.launch("text/*") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Download,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Importer un fichier CSV")
                        }
                    }
                }
            }

            // ── Section Catégories ──────────────────────────────
            item {
                Spacer(Modifier.height(4.dp))
                Text("Catégories", style = MaterialTheme.typography.titleMedium)
            }

            items(categories) { cat ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment    = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Text(cat.icon, style = MaterialTheme.typography.titleMedium)
                            Column {
                                Text(cat.name,
                                    style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    if (cat.isActive) "Active" else "Désactivée",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (cat.isActive)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked         = cat.isActive,
                            onCheckedChange = { vm.toggleCategory(cat) }
                        )
                    }
                }
            }
            }
        }
    }
}
