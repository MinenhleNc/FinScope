package vcmsa.projects.budgetbuddy.models

import androidx.room.Entity
import androidx.room.PrimaryKey

// Represents an expense logged by the user
@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Unique ID for the expense
    val description: String,                          // What the expense was for
    val amount: Double,                               // How much was spent
    val date: String,                                 // Date of the expense (YYYY-MM-DD)
    val startTime: String,                            // Start time for timed events
    val endTime: String,                              // End time for timed events
    val categoryId: Int,                              // Foreign key linking to Category
    val photoUri: String? = null                      // Optional photo of the receipt/item
)

