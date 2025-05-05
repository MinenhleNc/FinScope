package vcmsa.projects.budgetbuddy.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import vcmsa.projects.budgetbuddy.R

// Displays splash screen before app loads main login activity
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay for 2 seconds before launching login screen
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, LogInActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}


