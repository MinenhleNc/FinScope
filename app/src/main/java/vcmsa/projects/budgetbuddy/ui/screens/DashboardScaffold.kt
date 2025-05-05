package vcmsa.projects.budgetbuddy.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vcmsa.projects.budgetbuddy.activities.*
import vcmsa.projects.budgetbuddy.ui.components.DashboardHomeScreen

// Define all the screens accessible from the dashboard
enum class DashboardScreen {
    Home, Add, Categories, Expenses, Graphs
}

@Composable
fun DashboardScaffold(username: String) {
    // Color definitions
    val primaryPink = Color(0xFFE91E63)
    val lightPink = Color(0xFFFFC0CB)
    val white = Color.White
    val black = Color.Black
    val darkGray = Color(0xFF1E1E1E)

    var selectedScreen by remember { mutableStateOf(DashboardScreen.Home) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                current = selectedScreen,
                onSelected = { selectedScreen = it },
                primaryPink = primaryPink,
                lightPink = lightPink,
                white = white,
                darkGray = darkGray
            )
        },
        containerColor = black // Black background for scaffold
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedScreen) {
                DashboardScreen.Home -> DashboardHomeScreen(username)
                DashboardScreen.Add -> AddExpenseScreenNav()
                DashboardScreen.Categories -> CategoryScreenNav()
                DashboardScreen.Expenses -> ViewExpensesScreenNav()
                DashboardScreen.Graphs -> GraphScreenNav()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    current: DashboardScreen,
    onSelected: (DashboardScreen) -> Unit,
    primaryPink: Color,
    lightPink: Color,
    white: Color,
    darkGray: Color
) {
    NavigationBar(
        containerColor = darkGray, // Dark gray navigation bar
        contentColor = white // White content by default
    ) {
        NavigationBarItem(
            selected = current == DashboardScreen.Home,
            onClick = { onSelected(DashboardScreen.Home) },
            label = {
                Text(
                    "Home",
                    color = if (current == DashboardScreen.Home) primaryPink else white
                )
            },
            icon = {
                Text(
                    "🏠",
                    color = if (current == DashboardScreen.Home) primaryPink else white
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = primaryPink,
                selectedTextColor = primaryPink,
                indicatorColor = darkGray
            )
        )
        NavigationBarItem(
            selected = current == DashboardScreen.Add,
            onClick = { onSelected(DashboardScreen.Add) },
            label = {
                Text(
                    "Add",
                    color = if (current == DashboardScreen.Add) primaryPink else white
                )
            },
            icon = {
                Text(
                    "➕",
                    color = if (current == DashboardScreen.Add) primaryPink else white
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = primaryPink,
                selectedTextColor = primaryPink,
                indicatorColor = darkGray
            )
        )
        NavigationBarItem(
            selected = current == DashboardScreen.Categories,
            onClick = { onSelected(DashboardScreen.Categories) },
            label = {
                Text(
                    "Categories",
                    color = if (current == DashboardScreen.Categories) primaryPink else white
                )
            },
            icon = {
                Text(
                    "🗂️",
                    color = if (current == DashboardScreen.Categories) primaryPink else white
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = primaryPink,
                selectedTextColor = primaryPink,
                indicatorColor = darkGray
            )
        )
        NavigationBarItem(
            selected = current == DashboardScreen.Expenses,
            onClick = { onSelected(DashboardScreen.Expenses) },
            label = {
                Text(
                    "Expenses",
                    color = if (current == DashboardScreen.Expenses) primaryPink else white
                )
            },
            icon = {
                Text(
                    "📊",
                    color = if (current == DashboardScreen.Expenses) primaryPink else white
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = primaryPink,
                selectedTextColor = primaryPink,
                indicatorColor = darkGray
            )
        )
        NavigationBarItem(
            selected = current == DashboardScreen.Graphs,
            onClick = { onSelected(DashboardScreen.Graphs) },
            label = {
                Text(
                    "Graphs",
                    color = if (current == DashboardScreen.Graphs) primaryPink else white
                )
            },
            icon = {
                Text(
                    "📈",
                    color = if (current == DashboardScreen.Graphs) primaryPink else white
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = primaryPink,
                selectedTextColor = primaryPink,
                indicatorColor = darkGray
            )
        )
    }
}

@Composable
fun CategoryScreenNav() {
    AddCategoryScreenNav()
}