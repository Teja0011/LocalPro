package com.example.data.utils

data class CostBreakdown(
    val baseRate: Double,
    val durationHours: Double,
    val serviceCharge: Double,
    val emergencySurcharge: Double,
    val platformFee: Double,
    val totalCost: Double
)

object CostCalculator {
    const val PLATFORM_FEE_PERCENT = 0.05 // 5%
    const val EMERGENCY_SURCHARGE_PERCENT = 0.20 // 20%

    fun calculate(
        hourlyRate: Double,
        durationHours: Double,
        isEmergency: Boolean
    ): CostBreakdown {
        val serviceCharge = hourlyRate * durationHours
        val emergencySurcharge = if (isEmergency) serviceCharge * EMERGENCY_SURCHARGE_PERCENT else 0.0
        val subtotal = serviceCharge + emergencySurcharge
        val platformFee = subtotal * PLATFORM_FEE_PERCENT
        val totalCost = subtotal + platformFee

        return CostBreakdown(
            baseRate = hourlyRate,
            durationHours = durationHours,
            serviceCharge = serviceCharge,
            emergencySurcharge = emergencySurcharge,
            platformFee = platformFee,
            totalCost = totalCost
        )
    }

    fun formatCurrency(amount: Double): String {
        return "₹${amount.toInt()}"
    }
}
