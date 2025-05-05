package vcmsa.projects.budgetbuddy.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import vcmsa.projects.budgetbuddy.ui.screens.*
import vcmsa.projects.budgetbuddy.ui.theme.BudgetBuddyTheme

// Activity that launches the main dashboard after user logs in.
class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the username passed from the login activity
        val username = intent.getStringExtra("username") ?: "User"

        // Set up the content using Jetpack Compose and custom theme
        setContent {
            BudgetBuddyTheme {
                DashboardScaffold(username)
            }
        }
    }
}

