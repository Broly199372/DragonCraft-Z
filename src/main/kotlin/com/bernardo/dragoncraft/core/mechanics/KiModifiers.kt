package com.bernardo.dragoncraft.core.mechanics

import com.bernardo.dragoncraft.core.data.PlayerKiData
import kotlin.math.max
import kotlin.math.min

/**
 * Sistema de Modificadores para Ki.
 *
 * Aplica efeitos temporários e permanentes nos atributos de Ki.
 * Suporta: buffs, debuffs, amplificadores de poder, redução de dano, etc.
 */
object KiModifiers {

    fun applyPowerBuff(data: PlayerKiData, powerBonus: Float, durationTicks: Int) {
        data.kiPower *= powerBonus
    }

    fun applyDrainDebuff(data: PlayerKiData, drainMultiplier: Float, durationTicks: Int) {
        data.kiDrainRate *= drainMultiplier
    }

    fun reduceKiControl(data: PlayerKiData, reduction: Float) {
        data.kiControl = max(0.1f, data.kiControl - reduction)
    }

    fun increaseKiControl(data: PlayerKiData, amount: Float) {
        data.kiControl = min(1.0f, data.kiControl + amount)
    }

    fun increaseKiDefense(data: PlayerKiData, amount: Float) {
        data.kiDefense = (data.kiDefense + amount).coerceAtMost(5f)
    }

    fun restoreStamina(data: PlayerKiData, amount: Float) {
        data.currentStamina = min(data.maxStamina, data.currentStamina + amount)
    }

    fun exhaustStamina(data: PlayerKiData, amount: Float) {
        data.currentStamina = max(0f, data.currentStamina - amount)
    }

    fun onRapidActions(data: PlayerKiData) {
        data.consecutiveActionsCount++
        if (data.consecutiveActionsCount % 5 == 0) {
            data.kiControl *= 0.9f
        }
        if (data.consecutiveActionsCount > 50) {
            data.kiControl = 0.5f
            data.consecutiveActionsCount = 0
        }
    }

    fun resetFromExertion(data: PlayerKiData) {
        data.consecutiveActionsCount = 0
        data.kiControl = min(1.0f, data.kiControl + 0.1f)
    }

    fun applyWeaknessEffect(data: PlayerKiData): Float {
        val kiPercent = data.getKiPercent()
        if (kiPercent < 0.3f) {
            val weakness = (0.3f - kiPercent) * 2
            return 1f - weakness
        }
        return 1f
    }

    fun calculateTotalDrain(baseDrain: Float, data: PlayerKiData, multiplier: Float = 1f): Float {
        var totalDrain = baseDrain * multiplier
        totalDrain *= data.kiDrainRate
        totalDrain *= data.transformationState.drainMultiplier
        totalDrain *= data.race.drainPenalty
        return max(0f, totalDrain)
    }

    fun calculateTotalRegen(baseRegen: Float, data: PlayerKiData): Float {
        var totalRegen = baseRegen * data.kiRegenRate
        totalRegen *= data.currentKiState.regenMultiplier()
        totalRegen *= data.race.regenBonus
        return max(0f, totalRegen)
    }
}
