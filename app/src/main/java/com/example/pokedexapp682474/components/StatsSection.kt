import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatsSection(
    stats: Map<String, Int>, // Example: {"HP" to 39, "Attack" to 52, ...}
    maxStatValue: Int = 100 // Set the max value based on Pokémon stat limits
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        stats.forEach { (statName, statValue) ->
            StatRow(statName = statName, statValue = statValue, maxStatValue = maxStatValue)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun StatRow(
    statName: String,
    statValue: Int,
    maxStatValue: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Stat Name
        Text(
            text = statName,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.weight(1f) // Aligns the name to the left
        )

        // Stat Value
        Text(
            text = statValue.toString(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Progress Bar
        LinearProgressIndicator(
            progress = { statValue / maxStatValue.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .weight(2f), // Aligns the progress bar to take more space
            color = Color(0xFF6C63FF), // Purple progress bar color
            trackColor = Color(0xFFE0E0E0), // Gray background
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStatsComponent() {
    val stats = mapOf(
        "HP" to 39,
        "Attack" to 52,
        "Defense" to 43,
        "Special Attack" to 60,
        "Special Defense" to 50,
        "Speed" to 50
    )

    StatsSection(stats = stats, maxStatValue = 100)
}
