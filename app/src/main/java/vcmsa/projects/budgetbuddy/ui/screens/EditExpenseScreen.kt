package vcmsa.projects.budgetbuddy.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import vcmsa.projects.budgetbuddy.data.AppDatabase
import vcmsa.projects.budgetbuddy.models.Expense

@Composable
fun EditExpenseScreenNav(
    expense: Expense,           // Expense item passed in for editing
    onSave: () -> Unit,         // Callback when saving is successful
    onCancel: () -> Unit        // Callback for cancelling editing
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()

    // State variables prefilled with existing expense details
    var desc by remember { mutableStateOf(expense.description) }
    var amount by remember { mutableStateOf(expense.amount.toString()) }
    var date by remember { mutableStateOf(expense.date) }
    var startTime by remember { mutableStateOf(expense.startTime ?: "") }
    var endTime by remember { mutableStateOf(expense.endTime ?: "") }

    // Layout for the edit screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4CAF50))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Edit Expense", fontSize = 24.sp, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        // Description input field
        OutlinedTextField(
            value = desc,
            onValueChange = { desc = it },
            label = { Text("Description", color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Amount input field
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount (R)", color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(8.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Date input
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date (yyyy-MM-dd)", color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Time fields
        OutlinedTextField(
            value = startTime,
            onValueChange = { startTime = it },
            label = { Text("Start Time (HH:mm)", color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = endTime,
            onValueChange = { endTime = it },
            label = { Text("End Time (HH:mm)", color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons to save or cancel
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = {
                    val amt = amount.toDoubleOrNull()
                    if (desc.isNotBlank() && date.isNotBlank() && amt != null) {
                        scope.launch {
                            // Update the expense in the database
                            db.expenseDao().updateExpense(
                                expense.copy(
                                    description = desc,
                                    amount = amt,
                                    date = date,
                                    startTime = startTime,
                                    endTime = endTime
                                )
                            )
                            Toast.makeText(context, "Expense updated", Toast.LENGTH_SHORT).show()
                            onSave()
                        }
                    } else {
                        Toast.makeText(context, "Fill in all fields correctly", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = buttonColors(containerColor = Color.Black)
            ) {
                Text("💾 Save", color = Color.White)
            }

            Button(
                onClick = { onCancel() },
                colors = buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancel", color = Color.White)
            }
        }
    }
}


