package com.example.reshmenammapride

import java.util.*

class RecommendationEngine {

    fun analyze(email: String, batchName: String, entries: List<ClimateEntry>): Recommendation {
        if (entries.isEmpty()) {
            return Recommendation(
                userEmail = email,
                batchName = batchName,
                stage = "N/A",
                riskLevel = "Low",
                reason = "No data available",
                advice = "Start recording climate data to get personalized advice.",
                timestamp = System.currentTimeMillis()
            )
        }

        val latest = entries.first()
        val recentEntries = entries.take(7)
        
        val stage = latest.stage
        val temp = latest.temperature
        val hum = latest.humidity

        val idealRange = getIdealRange(stage)
        val adviceList = mutableListOf<String>()
        var riskLevel = "Low"
        val reasons = mutableListOf<String>()

        // 1. Current State Analysis
        if (temp !in idealRange.first) {
            if (temp < idealRange.first.start) {
                adviceList.add("Temperature is too low. Use heaters or close windows.")
                reasons.add("Low temperature")
            } else {
                adviceList.add("Temperature is too high. Increase ventilation or use cooling fans.")
                reasons.add("High temperature")
            }
            riskLevel = "Medium"
        }

        if (hum !in idealRange.second) {
            if (hum < idealRange.second.start) {
                adviceList.add("Humidity is low. Spray water lightly on the floor.")
                reasons.add("Low humidity")
            } else {
                adviceList.add("Humidity is high. Improve air circulation.")
                reasons.add("High humidity")
            }
            riskLevel = if (riskLevel == "Medium") "High" else "Medium"
        }

        // 2. Trend Analysis (Last 3-7 entries)
        if (recentEntries.size >= 3) {
            val tempTrend = checkTrend(recentEntries.map { it.temperature })
            val humTrend = checkTrend(recentEntries.map { it.humidity })

            if (tempTrend == "Increasing" && temp > idealRange.first.endInclusive) {
                adviceList.add("Temperature is increasing daily. Improve ventilation immediately.")
                reasons.add("Increasing temperature trend")
                riskLevel = "High"
            }

            if (humTrend == "Decreasing" && hum < idealRange.second.start) {
                adviceList.add("Humidity has been low for the last few entries. Spray water lightly.")
                reasons.add("Decreasing humidity trend")
                riskLevel = "High"
            }
        }

        // 3. Default Advice
        if (adviceList.isEmpty()) {
            adviceList.add("Current climate is suitable for $stage.")
            reasons.add("Stable conditions")
            riskLevel = "Low"
        }

        if (riskLevel == "High") {
            adviceList.add("High risk of crop stress. Check rearing room immediately.")
        }

        return Recommendation(
            userEmail = email,
            batchName = batchName,
            stage = stage,
            riskLevel = riskLevel,
            reason = reasons.joinToString(", "),
            advice = adviceList.distinct().joinToString(" "),
            timestamp = System.currentTimeMillis()
        )
    }

    private fun getIdealRange(stage: String): Pair<ClosedFloatingPointRange<Double>, ClosedFloatingPointRange<Double>> {
        return when (stage) {
            "1st Instar", "2nd Instar" -> 26.0..28.0 to 85.0..90.0
            "3rd Instar" -> 25.0..27.0 to 80.0..85.0
            "4th Instar", "5th Instar" -> 24.0..26.0 to 70.0..75.0
            else -> 23.0..25.0 to 65.0..70.0
        }
    }

    private fun checkTrend(values: List<Double>): String {
        if (values.size < 3) return "Stable"
        var increasing = 0
        var decreasing = 0
        for (i in 0 until values.size - 1) {
            if (values[i] > values[i+1]) decreasing++ // values are sorted DESC by timestamp, so i is newer than i+1
            if (values[i] < values[i+1]) increasing++
        }
        return when {
            increasing > values.size / 2 -> "Decreasing" // Latest is higher than older
            decreasing > values.size / 2 -> "Increasing" // Latest is lower than older
            else -> "Stable"
        }
        // Correction: if entries are [latest, older, oldest], then values[i] > values[i+1] means latest > older -> Increasing
    }
    
    // Fixed trend check for DESC sorted entries
    private fun checkTrendFixed(values: List<Double>): String {
        if (values.size < 3) return "Stable"
        var incCount = 0
        var decCount = 0
        for (i in 0 until values.size - 1) {
            // entries[i] is newer than entries[i+1]
            if (values[i] > values[i+1]) incCount++
            if (values[i] < values[i+1]) decCount++
        }
        return when {
            incCount >= 2 -> "Increasing"
            decCount >= 2 -> "Decreasing"
            else -> "Stable"
        }
    }
}
