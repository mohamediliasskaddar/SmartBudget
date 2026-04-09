I have completed the application and tried to design the UI myself with colors, but the result was not very satisfying. Could you please improve the UI to achieve a more consistent and attractive look while respecting the chosen color scheme? The goal is to have a more visually appealing and harmonious display
here is my actual ui parts,

```tree
**Root Path:** `c:\Users\imk\AndroidStudioProjects\SmartBudget\app\src\main\java\com\example\smartbudget\ui`

```
├── components
│   ├── BackgroundImage.kt
│   ├── CategoryChip.kt
│   ├── CategoryPicker.kt
│   ├── EmptyState.kt
│   ├── MonthNavigator.kt
│   └── TotalCard.kt
├── expenses
│   ├── AddEditExpenseSheet.kt
│   ├── ExpenseItem.kt
│   ├── ExpensesScreen.kt
│   └── ExpensesViewModel.kt
├── guide
│   └── ApplicationGuide.kt
├── navigation
│   ├── AppNavigation.kt
│   └── BottomNavBar.kt
├── settings
│   ├── SettingsScreen.kt
│   └── SettingsViewModel.kt
├── stats
│   ├── StatsScreen.kt
│   └── StatsViewModel.kt
├── theme
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
└── welcome
└── WelcomeScreen.kt
```

colors i have the file : y can add colors or even edit them the way u want, 

```

//here are the colors i wanna use in my app
val indigoPrimary = Color(0xFF2E31E7)
val blueAccent    = Color(0xFF4167FF)
val skyBlue       = Color(0xFF3FBCFC)

val white = Color(0xFFFFFFFF)
val black = Color(0xFF000000)

//charts colors
object ChartColors {
val alimentationColor  = Color(0xFF070838)
val logementColor      = Color(0xFF4B4C74)
val santeColor         = Color(0xFF2E31E7)
val transportColor     = Color(0xFF3FBCFC)
val loisirColor        = Color(0xFF4C7E98)
val etudeColor         = Color(0xFF5B74EB)
val autreColor         = Color(0xFFA4B3F8)
}
```

i will send u file by file and u give replace my colros with the one u see beter from teh colors.kt, if u see any part is good no need to change it keep it as it's 
let start by componenets:

```
// ui/components/BackgroundImage.kt
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
```


```
@Composable
fun CategoryChip(
category: CategoryEntity,
isSelected: Boolean,
onClick: () -> Unit,
modifier: Modifier = Modifier
) {
FilterChip(
selected = isSelected,
onClick  = onClick,
label    = { Text("${category.icon} ${category.name}") },
modifier = modifier.padding(end = 6.dp),
colors = FilterChipDefaults.filterChipColors(
selectedContainerColor = indigoPrimary,
selectedLabelColor = white,
containerColor = skyBlue.copy(alpha = 0.2f),
labelColor = indigoPrimary
)
)
}
```


```
@Composable
fun CategoryPicker(
categories: List<CategoryEntity>,
selectedId: Long?,
onSelect: (Long?) -> Unit,
modifier: Modifier = Modifier
) {
LazyRow(
modifier = modifier,
horizontalArrangement = Arrangement.spacedBy(4.dp),
contentPadding = PaddingValues(horizontal = 16.dp)
) {
item {
CategoryChip(
category = CategoryEntity(id = -1, name = "Tout", icon = "📋", color = "#607D8B"),
isSelected = selectedId == null,
onClick = { onSelect(null) }
)
}
items(categories) { cat ->
CategoryChip(
category   = cat,
isSelected = selectedId == cat.id,
onClick    = { onSelect(cat.id) }
)
}
}
}
```


```
@Composable
fun EmptyState(
message: String = "Aucune dépense ce mois-ci",
modifier: Modifier = Modifier
) {
Column(
modifier = modifier
.fillMaxSize()
.padding(32.dp),
verticalArrangement   = Arrangement.Center,
horizontalAlignment   = Alignment.CenterHorizontally
) {
Text(text = "💸", style = MaterialTheme.typography.displayMedium)
Spacer(modifier = Modifier.height(16.dp))
Text(
text      = message,
style     = MaterialTheme.typography.bodyLarge,
textAlign = TextAlign.Center,
color     = MaterialTheme.colorScheme.onSurfaceVariant
)
}
}
```


```
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
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onPrevious,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = white
            )
        ) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "Mois précédent")
        }
        Text(
            text = "$monthName $year",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = white
        )
        IconButton(
            onClick = onNext,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = white
            )
        ) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Mois suivant")
        }
    }
}
```


```
@Composable
fun TotalCard(
total: Double,
modifier: Modifier = Modifier,
currency: String = "MAD"
) {
Card(
modifier = modifier.fillMaxWidth(),
colors = CardDefaults.cardColors(
containerColor = indigoPrimary,
contentColor = white
),
elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
) {
Column(modifier = Modifier.padding(16.dp)) {
Text(
text = "Total du mois",
style = MaterialTheme.typography.labelMedium,
color = white.copy(alpha = 0.8f)
)
Spacer(modifier = Modifier.height(4.dp))
Text(
text = CurrencyUtils.format(total, currency),
style = MaterialTheme.typography.headlineMedium,
color = white
)
}
}
}

```

when u finish those parts i will send u the other fils screnns etc