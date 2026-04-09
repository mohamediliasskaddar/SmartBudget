package com.example.smartbudget.ui.guide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.smartbudget.ui.components.BackgroundImage
import com.example.smartbudget.ui.theme.indigoPrimary
import com.example.smartbudget.ui.theme.white

@Composable
fun ApplicationGuide(modifier: Modifier = Modifier) {
    BackgroundImage(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {

            // 🔷 HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = indigoPrimary,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "Guide de l'Application",
                    style = MaterialTheme.typography.headlineMedium,
                    color = white
                )
            }

            // 🔥 CONTENT (fix du problème ici)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(), // 👈 prend tout l’espace restant
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 100.dp // 👈 espace pour navbar + FAB
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    GuideSection(
                        title = "Comment ajouter une dépense",
                        content = "Appuyez sur le bouton '+' au centre de la barre de navigation inférieure pour ajouter une nouvelle dépense. Remplissez les détails (montant, catégorie, date) et appuyez sur Enregistrer."
                    )
                }

                item {
                    GuideSection(
                        title = "Modifier ou Supprimer une dépense",
                        content = "Sur l'écran des dépenses, appuyez sur une dépense pour la modifier. Pour la supprimer, appuyez longuement sur la dépense et confirmez la suppression."
                    )
                }

                item {
                    GuideSection(
                        title = "Consulter les statistiques",
                        content = "Allez à l'onglet 'Stats' pour voir vos dépenses sous forme de graphiques et analyses par catégorie pour mieux comprendre vos habitudes de dépenses."
                    )
                }

                item {
                    GuideSection(
                        title = "Paramètres",
                        content = "Accédez à l'onglet 'Paramètres' pour configurer les préférences de l'application, gérer les catégories et personnaliser votre expérience."
                    )
                }

                item {
                    GuideSection(
                        title = "Filtrer par catégorie",
                        content = "Sur l'écran des dépenses, utilisez les puces de catégorie pour filtrer vos dépenses par type. Cela facilite le suivi de chaque catégorie."
                    )
                }

                item {
                    GuideSection(
                        title = "Navigation mensuelle",
                        content = "Utilisez les flèches pour naviguer entre les mois et consulter vos dépenses passées ou futures. Le total du mois s'affichera automatiquement."
                    )
                }
            }
        }
    }
}
@Composable
fun GuideSection(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = indigoPrimary.copy(alpha = 0.15f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = indigoPrimary.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = indigoPrimary
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodySmall,
                color = white.copy(alpha = 0.9f)
            )
        }
    }
}

