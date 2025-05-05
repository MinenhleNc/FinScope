package vcmsa.projects.budgetbuddy.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vcmsa.projects.budgetbuddy.R
import vcmsa.projects.budgetbuddy.data.AppDatabase
import vcmsa.projects.budgetbuddy.models.Category

@Composable
fun AddCategoryScreenNav() {
    // Context and database initialization
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()

    // State variables for user input and category list
    var categoryName by remember { mutableStateOf("") }
    var minBudget by remember { mutableStateOf("") }
    var maxBudget by remember { mutableStateOf("") }
    var categoryList by remember { mutableStateOf(emptyList<Category>()) }

    // Load all saved categories on screen load
    LaunchedEffect(Unit) {
        categoryList = withContext(Dispatchers.IO) { db.categoryDao().getAllCategories() }
    }

    // Main screen layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000)) // Changed to black background
            .padding(horizontal = 24.dp)
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo at the top
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text("Add Category", color = Color(0xFFE91E63), fontSize = 24.sp) // Changed to pink text

        Spacer(modifier = Modifier.height(24.dp))

        // Input field for category name
        OutlinedTextField(
            value = categoryName,
            onValueChange = { categoryName = it },
            label = { Text("Category Name", color = Color.Black) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Input field for minimum budget
        OutlinedTextField(
            value = minBudget,
            onValueChange = { minBudget = it },
            label = { Text("Minimum Budget (R)", color = Color.Black) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Input field for maximum budget
        OutlinedTextField(
            value = maxBudget,
            onValueChange = { maxBudget = it },
            label = { Text("Maximum Budget (R)", color = Color.Black) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Save category button
        Button(
            onClick = {
                val min = minBudget.toDoubleOrNull()
                val max = maxBudget.toDoubleOrNull()
                if (categoryName.isNotBlank() && min != null && max != null && min <= max) {
                    scope.launch {
                        db.categoryDao().insertCategory(
                            Category(name = categoryName, minBudget = min, maxBudget = max)
                        )
                        categoryList = db.categoryDao().getAllCategories()
                        categoryName = ""
                        minBudget = ""
                        maxBudget = ""
                        Toast.makeText(context, "Category saved!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Fill all fields correctly", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = buttonColors(containerColor = Color.Black)
        ) {
            Text("💾 Save", color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // List of saved categories
        Text("Saved Categories", color = Color(0xFFE91E63), fontSize = 18.sp, modifier = Modifier.align(Alignment.Start)) // Changed to pink text

        LazyColumn {
            items(categoryList) { cat ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("📛 ${cat.name}", fontSize = 16.sp, color = Color.Black)
                        Text("💵 Min: R%.2f".format(cat.minBudget), color = Color.DarkGray)
                        Text("💵 Max: R%.2f".format(cat.maxBudget), color = Color.DarkGray)
                    }
                }
            }
        }
    }
}