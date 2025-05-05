package vcmsa.projects.budgetbuddy.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vcmsa.projects.budgetbuddy.R
import vcmsa.projects.budgetbuddy.data.AppDatabase
import java.util.*

class ExpenseEntry(val label: String, override val x: Float, override val y: Float) : ChartEntry {
    override fun withY(y: Float): ChartEntry = ExpenseEntry(label, x, y)
}

@Composable
fun GraphScreenNav() {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()

    val calendar = Calendar.getInstance()

    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var categoryNames by remember { mutableStateOf(emptyList<String>()) }
    var expensesByCategory by remember { mutableStateOf(emptyList<Float>()) }
    var modelProducer by remember { mutableStateOf(ChartEntryModelProducer(listOf(emptyList()))) }

    val colorPalette = listOf(
        Color(0xFFE91E63), Color(0xFF2196F3), Color(0xFFFF9800),
        Color(0xFF4CAF50), Color(0xFF9C27B0), Color(0xFFFF5722),
        Color(0xFF795548), Color(0xFF607D8B), Color(0xFF3F51B5)
    )

    suspend fun loadData() {
        withContext(Dispatchers.IO) {
            val categories = db.categoryDao().getAllCategories()
            val expenses = db.expenseDao().getAllExpenses()

            val filteredExpenses = expenses.filter {
                val entryDate = it.date
                (startDate.isEmpty() || entryDate >= startDate) &&
                        (endDate.isEmpty() || entryDate <= endDate)
            }

            val names = categories.map { it.name }
            val totals = categories.map { category ->
                filteredExpenses.filter { it.categoryId == category.id }
                    .sumOf { it.amount }
                    .toFloat()
            }

            categoryNames = names
            expensesByCategory = totals

            val allEntries = totals.mapIndexed { index, amount ->
                listOf(
                    ExpenseEntry(names.getOrNull(index) ?: "Unknown", index.toFloat(), amount)
                )
            }

            modelProducer = ChartEntryModelProducer(allEntries)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000)) // Changed to black background
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(60.dp)
                .padding(bottom = 16.dp)
        )

        Text("📊 Graphs", color = Color(0xFFE91E63), fontSize = 22.sp) // Changed to pink text

        Spacer(modifier = Modifier.height(16.dp))

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
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
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
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(if (endDate.isEmpty()) "End Date" else "📅 $endDate", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch { loadData() }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (expensesByCategory.isEmpty()) {
            Text("No data available", color = Color(0xFFE91E63)) // Changed to pink text
        } else {
            val columns = categoryNames.mapIndexed { index, _ ->
                LineComponent(
                    color = colorPalette[index % colorPalette.size].toArgb(),
                    thicknessDp = 24f,
                    shape = Shapes.roundedCornerShape(6)
                )
            }

            Chart(
                chart = columnChart(columns = columns),
                chartModelProducer = modelProducer,
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                runInitialAnimation = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text("Legend", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))

                categoryNames.forEachIndexed { index, name ->
                    val color = colorPalette[index % colorPalette.size]
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(color, shape = RoundedCornerShape(50))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(name, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}