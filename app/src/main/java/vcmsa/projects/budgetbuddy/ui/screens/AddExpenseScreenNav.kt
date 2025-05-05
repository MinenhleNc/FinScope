package vcmsa.projects.budgetbuddy.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import vcmsa.projects.budgetbuddy.R
import vcmsa.projects.budgetbuddy.data.AppDatabase
import vcmsa.projects.budgetbuddy.models.Category
import vcmsa.projects.budgetbuddy.models.Expense
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreenNav() {
    // Context and database setup
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()
    val calendar = Calendar.getInstance()

    // Load categories from the database
    val categories by produceState(initialValue = emptyList<Category>()) {
        value = db.categoryDao().getAllCategories()
    }

    // UI state
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var desc by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var expanded by remember { mutableStateOf(false) }

    // Image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000)) // Changed to black background
            .padding(horizontal = 24.dp)
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Add Expense", fontSize = 24.sp, color = Color(0xFFE91E63)) // Changed to pink text
        Spacer(modifier = Modifier.height(20.dp))

        // Input for description
        OutlinedTextField(
            value = desc,
            onValueChange = { desc = it },
            label = { Text("Description", color = Color.Black) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Input for amount
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount (R)", color = Color.Black) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Date and time pickers
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = {
                    DatePickerDialog(
                        context,
                        { _, y, m, d -> date = "%04d-%02d-%02d".format(y, m + 1, d) },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
                colors = buttonColors(containerColor = Color.White)
            ) {
                Text(if (date.isEmpty()) "📅 Date" else date, color = Color.Black)
            }

            Button(
                onClick = {
                    TimePickerDialog(
                        context,
                        { _, h, m -> startTime = "%02d:%02d".format(h, m) },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                },
                colors = buttonColors(containerColor = Color.White)
            ) {
                Text(if (startTime.isEmpty()) "⏱ Start" else startTime, color = Color.Black)
            }

            Button(
                onClick = {
                    TimePickerDialog(
                        context,
                        { _, h, m -> endTime = "%02d:%02d".format(h, m) },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                },
                colors = buttonColors(containerColor = Color.White)
            ) {
                Text(if (endTime.isEmpty()) "⏱ End" else endTime, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Category dropdown
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                readOnly = true,
                value = selectedCategory?.name ?: "",
                onValueChange = {},
                label = { Text("Select Category", color = Color.Black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .background(Color.White, RoundedCornerShape(8.dp)),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(8.dp),
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                colors = OutlinedTextFieldDefaults.colors()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            selectedCategory = category
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Image picker button
        Button(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth(),
            colors = buttonColors(containerColor = Color.White)
        ) {
            Text("📷 Pick Image", color = Color.Black)
        }

        // Show selected image preview
        imageUri?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Expense Photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Save expense button
        Button(
            onClick = {
                val amt = amount.toDoubleOrNull()
                if (desc.isNotBlank() && amt != null && date.isNotBlank() && selectedCategory != null) {
                    scope.launch {
                        db.expenseDao().insertExpense(
                            Expense(
                                description = desc,
                                amount = amt,
                                date = date,
                                startTime = startTime,
                                endTime = endTime,
                                categoryId = selectedCategory!!.id,
                                photoUri = imageUri?.toString()
                            )
                        )
                        // Reset fields after save
                        desc = ""
                        amount = ""
                        date = ""
                        startTime = ""
                        endTime = ""
                        selectedCategory = null
                        imageUri = null
                        Toast.makeText(context, "Expense saved", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Fill all required fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = buttonColors(containerColor = Color.Black)
        ) {
            Text("💾 Save Expense", color = Color.White)
        }
    }
}