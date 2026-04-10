# 💰 SmartBudget

> Application Android native de gestion de budget personnel — **offline-first**

![Android](https://img.shields.io/badge/Android-API%2026%2B-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.x-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024-4285F4?logo=jetpackcompose&logoColor=white)
![Room](https://img.shields.io/badge/Room-2.8.4-FF6F00)
![License](https://img.shields.io/badge/Licence-MIT-blue)

---

##  Aperçu

SmartBudget permet aux étudiants et jeunes actifs de **suivre leurs dépenses quotidiennes** de manière simple, rapide et entièrement hors-ligne.
[Voir toutes les captures d’écran](#captures-decran)

![Alt text](./screenshots/ui.png)

---

##  Fonctionnalités

### Principales : (statut : terminées)
- ✅ **CRUD complet** — Ajouter, modifier (tap), supprimer (appui long) une dépense
- ✅ **Catégorisation** — 7 catégories avec icône emoji (Alimentation, Transport, Logement…)
- ✅ **Navigation mensuelle** — Vue par mois avec boutons précédent / suivant
- ✅ **Filtrage** — Filtre par catégorie via chips horizontaux
- ✅ **Total du mois** — Carte récapitulative visible sur tous les écrans
- ✅ **Statistiques** — Camembert de répartition + classement par catégorie
- ✅ **Offline-first** — Aucune connexion internet requise

### Bonus : (statut : terminées)
- [x] **Budgets mensuels** — Limite par catégorie avec barre de progression et alerte dépassement
- [x] **Dépenses récurrentes** — Toggle pour marquer une dépense mensuelle automatique
- [x] **Export CSV** — Partage du mois courant via l'intent Android
- [x] **Import CSV** — Import de dépenses depuis un fichier externe

---

##  Architecture

```
SmartBudget/
├── data
│   ├── local
│   │   ├── dao
│   │   ├── entity
│   │   └── SmartBudgetDatabase.kt
│   ├── model
│   │   ├── Category.kt
│   │   ├── CategoryStats.kt
│   │   ├── Expense.kt
│   │   └── MonthlyBudget.kt
│   └── repository
│       ├── BudgetRepository.kt
│       ├── CategoryRepository.kt
│       └── ExpenseRepository.kt
├── di
│   ├── DatabaseModule.kt
│   └── RepositoryModule.kt
├── domain
│   └── usecase
│       ├── AddExpense.kt
│       ├── DeleteExpense.kt
│       ├── ExportMonthCsv.kt
│       ├── GetCategories.kt
│       ├── GetExpensesByMonth.kt
│       ├── GetMonthStats.kt
│       └── UpdateExpense.kt
├── ui
│   ├── components
│   │   ├── BackgroundImage.kt
│   │   ├── CategoryChip.kt
│   │   ├── CategoryPicker.kt
│   │   ├── EmptyState.kt
│   │   ├── MonthNavigator.kt
│   │   └── TotalCard.kt
│   ├── expenses
│   │   ├── AddEditExpenseSheet.kt
│   │   ├── ExpenseItem.kt
│   │   ├── ExpensesScreen.kt
│   │   └── ExpensesViewModel.kt
│   ├── guide
│   │   └── ApplicationGuide.kt
│   ├── navigation
│   │   ├── AppNavigation.kt
│   │   └── BottomNavBar.kt
│   ├── settings
│   │   ├── SettingsScreen.kt
│   │   └── SettingsViewModel.kt
│   ├── stats
│   │   ├── StatsScreen.kt
│   │   └── StatsViewModel.kt
│   ├── theme
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── welcome
│       └── WelcomeScreen.kt
├── util
│   ├── CsvExporter.kt
│   ├── CsvImporter.kt
│   ├── CurrencyUtils.kt
│   └── DateUtils.kt
├── AppDatabase.kt
├── MainActivity.kt
├── SmartBudgetApp.kt
└── init.sh   
```

Pattern : **MVVM + Clean Architecture**

```
UI (Compose) ──→ ViewModel ──→ UseCase ──→ Repository ──→ Room (SQLite)
                    ↑                           ↓
                 StateFlow               Flow<List<Entity>>
```

---

##  Stack technique

| Composant | Technologie | Version |
|-----------|-------------|---------|
| Langage | Kotlin | 1.9.x |
| UI | Jetpack Compose | BOM 2024 |
| Base de données | Room (SQLite) | 2.8.4 |
| Navigation | Navigation Compose | 2.7.7 |
| ViewModel | Lifecycle ViewModel Compose | 2.8.0 |
| Async | Kotlinx Coroutines + Flow | 1.7.3 |
| Graphiques | MPAndroidChart (PhilJay) | 3.1.0 |
| Build | Gradle KTS + KSP | — |
| Min SDK | Android 8.0 (Oreo) | API 26 |
| Target SDK | Android 14 | API 34 |


---
##  Modèle de données

```
CategoryEntity (1) ──< ExpenseEntity (N)
CategoryEntity (1) ──< MonthlyBudgetEntity (N)
```

- Suppression catégorie **interdite** si des dépenses existent (`RESTRICT`)
- Budget mensuel : suppression en `CASCADE`

---

##  Règles métier

- Montant strictement **positif**
- Date **obligatoire**
- Catégorie **obligatoire**
- Nom de catégorie **unique**
- Barre budget : 🟡 orange à 80%, 🔴 rouge si dépassement

---
##  Installation

### Prérequis
- Android Studio Hedgehog (2023.1.1) ou supérieur
- JDK 17
- Android SDK API 26+

### Cloner & lancer

```bash
git clone https://github.com/mohamediliasskaddar/SmartBudget.git

cd SmartBudget
```

Ouvrir le projet dans **Android Studio** puis :

```
File → Sync Project with Gradle Files
Run → Run 'app'
```

Et dans `settings.gradle.kts` :

```kotlin
maven { url = uri("https://jitpack.io") }  // pour MPAndroidChart
```

---
## Captures d'écran
![Alt text](./screenshots/frames.png)

##  Contribution

Les contributions sont les bienvenues !

1. Fork le projet
2. Crée une branche (`git checkout -b feature/ma-fonctionnalite`)
3. Commit tes changements (`git commit -m 'feat: ajoute X'`)
4. Push la branche (`git push origin feature/ma-fonctionnalite`)
5. Ouvre une Pull Request

---

##  Licence

Ce projet est sous licence **MIT** — contacter [Mohamed Iliass Kaddar](mailto:moahmediliassk@gmail.com). pour plus de détails.

---

<div align="center">
  <strong>Réalisé dans le cadre d'un mini-projet Android</strong><br>
  Jetpack Compose · Room · MVVM · Clean Architecture
</div>
