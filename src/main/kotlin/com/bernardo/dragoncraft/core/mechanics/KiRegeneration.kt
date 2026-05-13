package com.bernardo.dragoncraft.core.mechanics

import com.bernardo.dragoncraft.core.data.KiState
import com.bernardo.dragoncraft.core.data.PlayerKiData
import kotlin.math.max

/**
 * Sistema de Regeneração Inteligente de Ki.
 * 
 * Gerencia quando e quanto Ki é regenerado baseado no estado do jogador.
 * Performance: cálculos leves, sem alocações desnecessárias
 */
object KiRegeneration {
    
    /** Cooldown mínimo entre cheques de regen (em ticks) */
    private const val REGEN_TICK_INTERVAL = 2
    
    /**
     * Regenera Ki do jogador baseado em seu estado.
     * Chamado periodicamente pelo game loop.
     * 
     * @param data Dados do jogador
     * @param currentTick Tick atual do jogo
     * @return Quanto Ki foi regenerado
     */
    fun regenerateKi(data: PlayerKiData, currentTick: Long): Float {
        // Se já está com Ki máximo, não regener
        if (data.currentKi >= data.maxKi) {
            data.currentKi = data.maxKi
            return 0f
        }
        
        // Calcular taxa de regeneração base
        var regenRate = data.kiRegenRate
        
        // Aplicar multiplicador do estado
        regenRate *= data.currentKiState.regenMultiplier()
        
        // Aplicar bônus de raça
        regenRate *= data.race.regenBonus
        
        // Se tem penalidade de dano recente, reduzir regen
        if (data.damageNerfTicks > 0) {
            regenRate *= 0.5f  // 50% mais lento
            data.damageNerfTicks--
        }
        
        // Se muito Ki disparado para carga, penalizar regen
        if (data.isChargingKi) {
            regenRate = 0f  // Não regen enquanto carregando
        }
        
        // Se com pouca stamina (<30%), reduzir regen
        if (data.currentStamina < data.maxStamina * 0.3f) {
            regenRate *= 0.6f
        }
        
        // Garantir que regen não é negativa
        regenRate = max(0f, regenRate)
        
        // Adicionar Ki (apenas 1 tick a cada REGEN_TICK_INTERVAL)
        if (currentTick % REGEN_TICK_INTERVAL == 0L) {
            data.currentKi = (data.currentKi + regenRate).coerceAtMost(data.maxKi)
            return regenRate
        }
        
        return 0f
    }
    
    /**
     * Aumenta o máximo de Ki permanentemente (leveling)
     */
    fun increaseMaxKi(data: PlayerKiData, amount: Float) {
        data.maxKi = (data.maxKi + amount).coerceAtMost(9999f)
        if (data.currentKi > data.maxKi) {
            data.currentKi = data.maxKi
        }
    }
    
    /**
     * Aumenta a taxa de regeneração
     */
    fun increaseRegenRate(data: PlayerKiData, amount: Float) {
        data.kiRegenRate = (data.kiRegenRate + amount).coerceAtMost(50f)
    }
    
    /**
     * Retorna o quanto de Ki será regenerado na próxima tick
     * (útil para UI/debug)
     */
    fun estimateNextRegen(data: PlayerKiData): Float {
        if (data.currentKi >= data.maxKi) return 0f
        
        var regenRate = data.kiRegenRate
        regenRate *= data.currentKiState.regenMultiplier()
        regenRate *= data.race.regenBonus
        
        if (data.damageNerfTicks > 0) {
            regenRate *= 0.5f
        }
        if (data.isChargingKi) {
            regenRate = 0f
        }
        if (data.currentStamina < data.maxStamina * 0.3f) {
            regenRate *= 0.6f
        }
        
        return max(0f, regenRate)
    }
}
