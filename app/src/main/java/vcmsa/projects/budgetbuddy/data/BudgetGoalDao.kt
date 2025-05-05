package vcmsa.projects.budgetbuddy.data

import androidx.room.*
import vcmsa.projects.budgetbuddy.models.BudgetGoal

// DAO for budget goal table (single-row use case)
@Dao
interface BudgetGoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setGoal(goal: BudgetGoal)

    @Query("SELECT * FROM budget_goal WHERE id = 0")
    suspend fun getGoal(): BudgetGoal?
}

