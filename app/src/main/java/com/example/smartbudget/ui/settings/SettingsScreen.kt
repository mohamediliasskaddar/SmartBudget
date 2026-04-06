// ui/settings/SettingsScreen.kt
package com.example.smartbudget.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartbudget.SmartBudgetApp

@Composable
fun SettingsScreen() {
    val app = LocalContext.current.applicationContext as SmartBudgetApp
    val vm: SettingsViewModel = viewModel(
        factory = SettingsViewModel.Factory(app.categoryRepository)
    )
    val categories by vm.categories.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text  = "Catégories",
            style = MaterialTheme.typography.titleLarge
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { cat ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(cat.icon,
                                style = MaterialTheme.typography.titleMedium)
                            Text(cat.name,
                                style = MaterialTheme.typography.bodyMedium)
                        }
                        Switch(
                            checked  = cat.isActive,
                            onCheckedChange = { vm.toggleCategory(cat) }
                        )
                    }
                }
            }
        }
    }
}