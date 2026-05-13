package com.bernardo.dragoncraft.core.mechanics

import com.bernardo.dragoncraft.core.data.KiState
import com.bernardo.dragoncraft.core.data.PlayerKiData
import net.minecraft.entity.player.PlayerEntity
import kotlin.math.max
import kotlin.math.sqrt

/**
 * Sistema de Drenagem Dinâmica de Ki.
 * 
 * Gerencia quando e quanto Ki é consumido durante diferentes ações.
 * Performance: evita cálculos pesados, cache de valores quando possível
 */
object KiDrain {
    
    // --- CONSTANTES DE DRENAGEM ---
    private const val DRAIN_FLYING = 0.8f          // Ki/tick voando
    private const val DRAIN_DASH = 5.0f            // Ki por dash
    private const val DRAIN_ATTACK = 8.0f          // Ki por ataque
    private const val DRAIN_CHARGE_TICK = 0.3f     // Ki/tick carregando
    private const val DRAIN_TRANSFORMATION = 1.2f  // Ki/tick com forma ativa
    private const val DRAIN_AURA = 0.4f            // Ki/tick com aura
    
    /**
     * Drena Ki por voar continuamente
     */
    fun drainFlying(data: PlayerKiData, flyingTicks: Int): Float {
        if (data.currentKiState != KiState.FLYING) return 0f
        
        var drain = DRAIN_FLYING * flyingTicks
        drain *= data.transformationState.drainMultiplier
        drain *= data.race.drainPenalty
        
        return consumeKi(data, drain)
    }
    
    /**
     * Drena Ki ao fazer um dash/movimento rápido
     */
    fun drainDash(data: PlayerKiData): Boolean {
        val drainAmount = DRAIN_DASH * data.transformationState.drainMultiplier
        
        if (data.currentKi < drainAmount) {
            return false  // Não tem Ki suficiente
        }
        
        consumeKi(data, drainAmount)
        data.currentKiState = KiState.DASHING
        return true
    }
    
    /**
     * Drena Ki ao atacar
     */
    fun drainAttack(data: PlayerKiData, attackPower: Float = 1.0f): Boolean {
        var drainAmount = DRAIN_ATTACK * attackPower
        drainAmount *= data.transformationState.drainMultiplier
        drainAmount *= data.race.drainPenalty
        
        if (data.currentKi < drainAmount) {
            return false
        }
        
        consumeKi(data, drainAmount)
        data.lastAttackTime = System.currentTimeMillis()
        data.currentKiState = KiState.ATTACKING
        return true
    }
    
    /**
     * Drena Ki por carregamento contínuo
     */
    fun drainCharging(data: PlayerKiData, ticksCharging: Int): Float {
        if (!data.isChargingKi) return 0f
        
        var drain = DRAIN_CHARGE_TICK * ticksCharging
        drain *= data.transformationState.drainMultiplier
        
        return consumeKi(data, drain)
    }
    
    /**
     * Drena Ki continuamente para manter transformação ativa
     */
    fun drainTransformation(data: PlayerKiData): Float {
        if (!data.transformationState.isActive()) return 0f
        
        val drain = DRAIN_TRANSFORMATION * data.transformationState.drainMultiplier
        return consumeKi(data, drain)
    }
    
    /**
     * Drena Ki para manter aura ativa
     */
    fun drainAura(data: PlayerKiData): Float {
        if (!data.auraActive) return 0f
        
        var drain = DRAIN_AURA * data.auraIntensity
        return consumeKi(data, drain)
    }
    
    /**
     * Usa Ki específico para uma habilidade customizada
     */
    fun drainCustom(data: PlayerKiData, amount: Float): Boolean {
        if (data.currentKi < amount) {
            return false
        }
        consumeKi(data, amount)
        return true
    }
    
    /**
     * Consome Ki do jogador (método central)
     * Retorna quanto foi realmente drenado
     */
    private fun consumeKi(data: PlayerKiData, amount: Float): Float {
        val actualDrain = minOf(amount, data.currentKi)
        data.currentKi = (data.currentKi - actualDrain).coerceAtLeast(0f)
        return actualDrain
    }
    
    /**
     * Calcula dano recebido baseado em Ki atual (quando leva dano físico)
     * Menos Ki = mais dano recebido
     */
    fun onPlayerDamaged(data: PlayerKiData, baseDamage: Float): Float {
        // Começar penalty de regen
        data.damageNerfTicks = 20  // 1 segundo (~20 ticks)
        
        // Poder de defesa reduz dano
        val actualDamage = baseDamage * (1f - (data.kiDefense * 0.1f))
        
        // Se tem pouco Ki, dano aumenta
        if (data.currentKi < data.maxKi * 0.2f) {
            return actualDamage * 1.3f
        }
        
        return actualDamage
    }
    
    /**
     * Estima quanto Ki será drenado na próxima tick
     * (útil para UI/debug)
     */
    fun estimateNextDrain(data: PlayerKiData): Float {
        var totalDrain = 0f
        
        if (data.currentKiState == KiState.FLYING) {
            totalDrain += DRAIN_FLYING * data.transformationState.drainMultiplier
        }
        
        if (data.isChargingKi) {
            totalDrain += DRAIN_CHARGE_TICK * data.transformationState.drainMultiplier
        }
        
        if (data.transformationState.isActive()) {
            totalDrain += DRAIN_TRANSFORMATION
        }
        
        if (data.auraActive) {
            totalDrain += DRAIN_AURA * data.auraIntensity
        }
        
        return totalDrain
    }
}
