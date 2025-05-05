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
import vcmsa.projects.budgetbuddy.data.User

class SignUpActivity : AppCompatActivity() {

    private lateinit var nameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var confirmPasswordField: EditText
    private lateinit var signupButton: Button
    private lateinit var backToLoginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        initViews()
        setupButtons()
    }

    private fun initViews() {
        nameField = findViewById(R.id.editTextName)
        emailField = findViewById(R.id.editTextEmail)
        passwordField = findViewById(R.id.editTextPassword)
        confirmPasswordField = findViewById(R.id.editTextConfirmPassword)
        signupButton = findViewById(R.id.buttonSignup)
        backToLoginButton = findViewById(R.id.buttonBackToLogin)
    }

    private fun setupButtons() {
        signupButton.setOnClickListener { handleSignUp() }
        backToLoginButton.setOnClickListener { navigateToLogin() }
    }

    private fun handleSignUp() {
        val name = nameField.text.toString().trim()
        val email = emailField.text.toString().trim()
        val password = passwordField.text.toString().trim()
        val confirmPassword = confirmPasswordField.text.toString().trim()

        when {
            name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                showToast("Please fill in all fields")
            }
            password != confirmPassword -> {
                showToast("Passwords do not match")
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast("Please enter a valid email")
            }
            else -> {
                registerUser(name, email, password)
            }
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val userDao = AppDatabase.getDatabase(this@SignUpActivity).userDao()

            if (userDao.getUserByEmail(email) != null) {
                showToast("User already exists")
            } else {
                userDao.insertUser(User(name = name, email = email, password = password))
                showToast("Account created!")
                navigateToLogin(clearStack = true)
            }
        }
    }

    private fun navigateToLogin(clearStack: Boolean = false) {
        val intent = Intent(this, LogInActivity::class.java)
        if (clearStack) {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        if (clearStack) finish()
    }

    private fun showToast(message: String) {
        runOnUiThread { Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_SHORT).show() }
    }
}