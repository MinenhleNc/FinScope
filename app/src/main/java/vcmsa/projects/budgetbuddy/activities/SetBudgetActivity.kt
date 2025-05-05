package vcmsa.projects.budgetbuddy.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import vcmsa.projects.budgetbuddy.data.AppDatabase
import vcmsa.projects.budgetbuddy.models.BudgetGoal
import vcmsa.projects.budgetbuddy.ui.theme.BudgetBuddyTheme

// Activity for setting a monthly budget goal
class SetBudgetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(applicationContext)

        // Use Jetpack Compose UI
        setContent {
            BudgetBuddyTheme {
                var goalText by remember { mutableStateOf("") }
                val scope = rememberCoroutineScope()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF4CAF50))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Set Monthly Budget Goal",
                        fontSize = 22.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Input field for budget amount
                    OutlinedTextField(
                        value = goalText,
                        onValueChange = { goalText = it },
                        label = { Text("Goal Amount (R)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Save button
                    Button(
                        onClick = {
                            val goal = goalText.toDoubleOrNull()
                            if (goal != null) {
                                scope.launch {
                                    db.budgetGoalDao().setGoal(BudgetGoal(goalAmount = goal))
                                    Toast.makeText(this@SetBudgetActivity, "Goal saved", Toast.LENGTH_SHORT).show()
                                    finish() // Return to previous screen
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text("💾 Save", color = Color.White)
                    }
                }
            }
        }
    }
}

