package vcmsa.projects.budgetbuddy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import vcmsa.projects.budgetbuddy.models.Category
import vcmsa.projects.budgetbuddy.models.Expense
import vcmsa.projects.budgetbuddy.models.BudgetGoal

// Main Room database for the BudgetBuddy app
@Database(
    entities = [User::class, Category::class, Expense::class, BudgetGoal::class],
    version = 6
)
abstract class AppDatabase : RoomDatabase() {

    // DAO access methods for the database
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun budgetGoalDao(): BudgetGoalDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        // Singleton pattern to ensure only one instance of the database is created
        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budgetbuddy_db"
                )
                    .fallbackToDestructiveMigration() // Rebuild database on schema changes
                    .build()
                    .also { instance = it }
            }
    }
}

