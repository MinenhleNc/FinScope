package vcmsa.projects.budgetbuddy.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Simple placeholder activity for screens not yet implemented
class PlaceholderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Render a "coming soon" message
        setContent {
            Column(modifier = Modifier.padding(32.dp)) {
                Text(text = "🚧 Coming Soon", fontSize = 24.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "This screen is under construction.")
            }
        }
    }
}

