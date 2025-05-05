package vcmsa.projects.budgetbuddy.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vcmsa.projects.budgetbuddy.R
import vcmsa.projects.budgetbuddy.data.AppDatabase

// Activity for user authentication (Login)
class LogInActivity : AppCompatActivity() {

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Link layout fields
        emailField = findViewById(R.id.editTextEmail)
        passwordField = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        signUpText = findViewById(R.id.textViewSignUp)

        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        // Login logic
        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                val user = userDao.getUserByEmail(email)
                runOnUiThread {
                    when {
                        user == null -> Toast.makeText(this@LogInActivity, "User not found", Toast.LENGTH_SHORT).show()
                        user.password != password -> Toast.makeText(this@LogInActivity, "Incorrect password", Toast.LENGTH_SHORT).show()
                        else -> {
                            Toast.makeText(this@LogInActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                            // Navigate to dashboard
                            val intent = Intent(this@LogInActivity, DashboardActivity::class.java)
                            intent.putExtra("username", user.name)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }

        // Navigate to sign-up screen
        signUpText.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}
