# SmartBudget Theme Implementation Guide

## 🎨 Color Palette Overview

### Primary Colors

```
┌─────────────────────────────────────────────┐
│ Indigo Primary (0xFF2E31E7)                │
│ Main color for interactive elements        │
│ Used in: Buttons, Navigation, TopAppBar   │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│ Blue Accent (0xFF4167FF)                   │
│ Secondary interactive elements             │
│ Used in: Secondary buttons, Accents       │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│ Sky Blue (0xFF3FBCFC)                      │
│ Tertiary elements and highlights           │
│ Used in: Tertiary buttons, Highlights     │
└─────────────────────────────────────────────┘
```

### Chart Category Colors

```
ChartColors object defines colors per expense category:

• santeColor (Health)         → 0xFF2E31E7 (Indigo)
• transportColor (Transport)  → 0xFF3FBCFC (Sky Blue)
• etudeColor (Education)      → 0xFF5B74EB (Purple-Blue)
• alimentationColor (Food)    → 0xFF070838 (Dark Navy)
• loisirColor (Entertainment) → 0xFF4C7E98 (Slate Blue)
• logementColor (Housing)     → 0xFF4B4C74 (Indigo-Gray)
• autreColor (Other)          → 0xFFA4B3F8 (Light Purple)
```

---

## 🌓 Light & Dark Mode

### Automatic Theme Detection
Colors automatically adjust based on system theme preference:

**Light Mode:**
- Primary buttons: Bright indigo on light background
- Text: Dark on light surface
- Overlay on background images: 35% opacity (lighter)

**Dark Mode:**
- Primary buttons: Sky blue on dark background
- Text: Light on dark surface
- Overlay on background images: 50% opacity (darker for better readability)

---

## 📱 How to Use Colors in New Components

### Example 1: Styling a Button
```kotlin
Button(
    onClick = { /* action */ },
    colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )
) {
    Text("Action")
}
```

### Example 2: Styling a Card
```kotlin
Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    )
) {
    // Card content
}
```

### Example 3: Using Chart Colors
```kotlin
val colors = listOf(
    ChartColors.santeColor.toArgb(),
    ChartColors.transportColor.toArgb(),
    ChartColors.etudeColor.toArgb()
)
dataSet.setColors(*colors.toIntArray())
```

### Example 4: Background Image Integration
```kotlin
@Composable
fun MyScreen() {
    BackgroundImage {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Your content here
        }
    }
}
```

---

## 🎯 Material3 Color System Mappings

| Purpose | Light Mode | Dark Mode | Usage |
|---------|-----------|----------|-------|
| **primary** | Indigo | Sky Blue | Main interactive elements |
| **onPrimary** | White | Black | Text on primary background |
| **primaryContainer** | Indigo 15% | Indigo 70% | Container backgrounds |
| **secondary** | Blue Accent | Blue Accent | Secondary actions |
| **tertiary** | Sky Blue | Sky Blue | Tertiary elements |
| **surface** | White | Dark gray | Card backgrounds |
| **onSurface** | Black | Light gray | Text on surface |
| **background** | Off-white | Almost black | Screen background |
| **error** | Red (#B00020) | Light red | Error states |

---

## 🖼️ Background Image Usage

### Available in:
- ✅ ExpensesScreen
- ✅ StatsScreen
- ✅ SettingsScreen
- ❌ WelcomeScreen (custom background only)

### Customizing Overlay Opacity

```kotlin
BackgroundImage(
    overlayOpacity = 0.4f  // Custom opacity (0f = transparent, 1f = opaque)
) {
    // Content
}
```

Default opacity (if not specified):
- Light mode: 0.35f
- Dark mode: 0.50f

---

## 🔧 Accessibility Guidelines

### Text Contrast
- All text on colored backgrounds maintains WCAG AA contrast ratios
- Background image overlay ensures text readability
- Overlay opacity increases automatically in dark mode

### Touch Targets
- Minimum touch target size: 48.dp x 48.dp
- Button/clickable element padding: 12.dp
- Spacing between elements: 8-16.dp

### Color Blindness Support
- Colors are not the only indicator (icons + text also used)
- Sufficient contrast for colorblind users
- Material icons used for visual differentiation

---

## 📊 Chart Color Assignment Logic

Charts use category-based colors in a cyclic pattern:

```
Category Index → Color Assignment
0 (first)      → santeColor
1              → transportColor
2              → etudeColor
3              → alimentationColor
4              → loisirColor
5              → logementColor
6              → autreColor
7 (and beyond) → Cycle repeats from santeColor
```

This ensures:
- Consistent colors for same categories across sessions
- Visual differentiation between categories
- Professional, cohesive chart appearance

---

## 🎨 Extending the Theme

### Adding New Colors
1. Define in `Color.kt`:
```kotlin
val myNewColor = Color(0xFFHEXVALUE)
```

2. Add to Theme.kt if it's a theme color:
```kotlin
private val LightColorScheme = lightColorScheme(
    // ...
    myCustomColor = myNewColor
)
```

### Creating Theme-Aware Components
```kotlin
@Composable
fun MyThemedComponent() {
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant
    
    Box(
        modifier = Modifier.background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Themed Text", color = textColor)
    }
}
```

---

## 🚀 Best Practices

1. **Always use MaterialTheme colors** instead of hardcoded values
   - Ensures consistency across app
   - Automatic dark mode support
   - Future theme customization easier

2. **Use proper color naming**
   - `onBackground` for text on backgrounds
   - `onSurface` for text on cards
   - `onPrimary` for text on primary buttons

3. **Test in both themes**
   - Preview light and dark modes
   - Verify readability and contrast
   - Check background image overlay visibility

4. **Respect overlay opacity**
   - Don't disable background images for text readability
   - Use overlay opacity parameter if needed
   - Let system auto-adjust based on theme

5. **Use Chart Colors consistently**
   - Always import from `ChartColors` object
   - Convert to Android Int with `.toArgb()`
   - Keep color assignments predictable

---

## 📚 Related Files

- **Color Definitions:** `ui/theme/Color.kt`
- **Theme System:** `ui/theme/Theme.kt`
- **Background Component:** `ui/components/BackgroundImage.kt`
- **Navigation Colors:** `ui/navigation/BottomNavBar.kt`
- **Component Examples:**
  - `ui/components/TotalCard.kt`
  - `ui/components/CategoryChip.kt`
  - `ui/expenses/ExpenseItem.kt`

---

## 🔍 Verification Checklist

- [ ] All screens use MaterialTheme colors
- [ ] Dark mode toggle works correctly
- [ ] Background images display on all screens (except Welcome)
- [ ] Text contrast meets WCAG AA standards
- [ ] Charts use ChartColors object
- [ ] Navigation bar shows primary colors
- [ ] TopAppBar uses primary colors
- [ ] No hardcoded color values (except in Color.kt)
- [ ] All components respect theme changes
- [ ] Overlay opacity adjusts per theme mode

