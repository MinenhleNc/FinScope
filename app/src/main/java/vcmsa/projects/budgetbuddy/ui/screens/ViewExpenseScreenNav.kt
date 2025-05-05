package vcmsa.projects.budgetbuddy.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import vcmsa.projects.budgetbuddy.R
import vcmsa.projects.budgetbuddy.data.AppDatabase
import vcmsa.projects.budgetbuddy.models.Expense
import java.util.*
import kotlin.coroutines.resume

@Composable
fun ViewExpensesScreenNav() {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()
    val calendar = Calendar.getInstance()

    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var expenses by remember { mutableStateOf(emptyList<Expense>()) }
    var hasSearched by remember { mutableStateOf(false) }
    var selectedExpense by remember { mutableStateOf<Expense?>(null) }
    var categoryTotals by remember { mutableStateOf<Map<String, Double>>(emptyMap()) }

    if (selectedExpense != null) {
        EditExpenseScreenNav(
            expense = selectedExpense!!,
            onSave = {
                selectedExpense = null
                scope.launch {
                    expenses = db.expenseDao().getAllExpenses()
                        .filter { it.date >= startDate && it.date <= endDate }
                        .sortedByDescending { it.date }
                }
            },
            onCancel = {
                selectedExpense = null
            }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000)) // Changed to black background
            .padding(horizontal = 16.dp, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("View Expenses", fontSize = 24.sp, color = Color(0xFFE91E63)) // Changed to pink text

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    DatePickerDialog(
                        context,
                        { _, y, m, d -> startDate = "%04d-%02d-%02d".format(y, m + 1, d) },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
                colors = buttonColors(containerColor = Color.White)
            ) {
                Text(if (startDate.isEmpty()) "Start Date" else "📅 $startDate", color = Color.Black)
            }

            Button(
                onClick = {
                    DatePickerDialog(
                        context,
                        { _, y, m, d -> endDate = "%04d-%02d-%02d".format(y, m + 1, d) },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
                colors = buttonColors(containerColor = Color.White)
            ) {
                Text(if (endDate.isEmpty()) "End Date" else "📅 $endDate", color = Color.Black)
            }

            Button(
                onClick = {
                    if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                        scope.launch {
                            val allExpenses = db.expenseDao().getAllExpenses()
                                .filter { it.date >= startDate && it.date <= endDate }

                            val grouped = allExpenses.groupBy { it.categoryId }
                            val categories = db.categoryDao().getAllCategories().associateBy { it.id }

                            val totals = grouped.mapNotNull { (catId, exps) ->
                                val name = categories[catId]?.name
                                val total = exps.sumOf { it.amount }
                                if (name != null) name to total else null
                            }.toMap()

                            expenses = allExpenses.sortedByDescending { it.date }
                            categoryTotals = totals
                            hasSearched = true
                        }
                    }
                },
                colors = buttonColors(containerColor = Color.White)
            ) {
                Text("Search", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (categoryTotals.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text("📊 Total Spent Per Category", fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                categoryTotals.forEach { (name, total) ->
                    Text("📛 $name: R%.2f".format(total), color = Color.DarkGray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (expenses.isEmpty() && hasSearched) {
            Text(
                "⚠️ No expenses found",
                color = Color(0xFFE91E63), // Changed to pink text
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        LazyColumn {
            items(expenses) { expense ->
                ExpenseCard(
                    expense = expense,
                    onEdit = { selectedExpense = it },
                    onDelete = {
                        scope.launch {
                            val confirm = confirmDelete(context)
                            if (confirm) {
                                db.expenseDao().deleteExpense(it)
                                expenses = expenses.filter { e -> e.id != it.id }
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ExpenseCard(
    expense: Expense,
    onEdit: (Expense) -> Unit,
    onDelete: (Expense) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📅 ${expense.date} (${expense.startTime} - ${expense.endTime})", fontSize = 14.sp, color = Color.Black)
            Text("💸 R${expense.amount}", fontSize = 16.sp, color = Color.Black)
            Text("📝 ${expense.description}", fontSize = 16.sp, color = Color.Black)

            if (!expense.photoUri.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = rememberAsyncImagePainter(expense.photoUri),
                    contentDescription = "Attached Photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onEdit(expense) },
                    colors = buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text("✏️ Edit", color = Color.White)
                }

                Button(
                    onClick = { onDelete(expense) },
                    colors = buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("🗑 Delete", color = Color.White)
                }
            }
        }
    }
}

suspend fun confirmDelete(context: Context): Boolean {
    return suspendCancellableCoroutine { cont ->
        AlertDialog.Builder(context)
            .setTitle("Delete Expense")
            .setMessage("Are you sure you want to delete this expense?")
            .setPositiveButton("Delete") { _, _ -> cont.resume(true) }
            .setNegativeButton("Cancel") { _, _ -> cont.resume(false) }
            .setOnCancelListener { cont.resume(false) }
            .show()
    }
}