package vcmsa.projects.budgetbuddy.models

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity class representing an expense category (e.g., Food, Transport, etc.)
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Unique ID for each category (auto-generated)
    val name: String,                                  // Name of the category
    val minBudget: Double = 0.0,                       // Minimum recommended budget for this category
    val maxBudget: Double = 0.0                        // Maximum allowed budget for this category
)


