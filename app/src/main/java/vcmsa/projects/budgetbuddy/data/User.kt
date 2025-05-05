package vcmsa.projects.budgetbuddy.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Data class for user table
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val password: String
)

