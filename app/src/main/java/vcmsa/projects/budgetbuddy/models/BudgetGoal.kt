package vcmsa.projects.budgetbuddy.models

import androidx.room.Entity
import androidx.room.PrimaryKey

// Represents the monthly budget goal set by the user.
// Since this is a single-value table, we keep the primary key fixed to 0.
@Entity(tableName = "budget_goal")
data class BudgetGoal(
    @PrimaryKey val id: Int = 0, // Always single row with id = 0
    val goalAmount: Double       // Target amount the user wants to stay within monthly
)

