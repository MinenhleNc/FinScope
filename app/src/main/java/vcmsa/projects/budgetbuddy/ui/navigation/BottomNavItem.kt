package vcmsa.projects.budgetbuddy.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a sealed class that defines all bottom navigation items
 * used in the Dashboard's bottom navigation bar.
 *
 * Each item contains:
 * - a `label` displayed to the user
 * - an `icon` as an emoji (used in text-based nav)
 * - a `route` used by the NavController for composable navigation
 */
sealed class BottomNavItem(
    val label: String,
    val icon: String,
    val route: String
) {
    // Home/Dashboard screen
    object Dashboard : BottomNavItem("Home", "🏠", "dashboard")

    // Add expense screen
    object AddExpense : BottomNavItem("Add", "➕", "add_expense")

    // Add or manage categories
    object AddCategory : BottomNavItem("Category", "📂", "add_category")

    // Graph screen (spending insights)
    object Graphs : BottomNavItem("Graphs", "📊", "graphs")

    // List of all expenses
    object ViewExpenses : BottomNavItem("Expenses", "📋", "view_expenses")
}
