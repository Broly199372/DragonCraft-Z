package com.bernardo.dragoncraft.core

import com.bernardo.dragoncraft.core.aura.AuraSystem
import com.bernardo.dragoncraft.core.data.KiState
import com.bernardo.dragoncraft.core.data.PlayerKiData
import com.bernardo.dragoncraft.core.mechanics.KiDrain
import com.bernardo.dragoncraft.core.mechanics.KiModifiers
import com.bernardo.dragoncraft.core.mechanics.KiRegeneration
import com.bernardo.dragoncraft.core.transformation.TransformationManager
import net.minecraft.entity.player.PlayerEntity

/**
 * SISTEMA DE KI - CÉREBRO CENTRAL
 * 
 * KiSystem é a classe que gerencia TODAS as operações de Ki de UM jogador.
 * É uma fachada que orquestra todos os subsistemas.
 * 
 * Responsabilidades:
 * - Coordenar regen, drain, modificadores
 * - Transformações e aura
 * - Validações de Ki
 * - Cálculos de poder
 * - Atualização de estado a cada tick
 * 
 * IMPORTANTE: Não possui estado! Todos os dados estão em PlayerKiData.
 * This é um gerenciador stateless que processa dados externos.
 */
object KiSystem {
    
    /**
     * Atualiza KI a cada game tick.
     * CHAMADO SEMPRE que o jogo faz um tick do servidor/cliente.
     * 
     * @param player O jogador Minecraft
     * @param data Dados de Ki do jogador
     * @param currentTick Tick atual
     */
    fun updateKi(player: PlayerEntity, data: PlayerKiData, currentTick: Long) {
        // --------- REGENERAÇÃO ---------
        KiRegeneration.regenerateKi(data, currentTick)
        
        // --------- DRENAGEM ---------
        // Drenagem por voo
        if (player.abilities.flying || player.isFallFlying) {
            val flyingTicks = 1
            KiDrain.drainFlying(data, flyingTicks)
            data.currentKiState = KiState.FLYING
        }
        
        // Drenagem por transformação ativa
        TransformationManager.updateTransformation(data, currentTick)
        
        // Drenagem por aura ativa
        KiDrain.drainAura(data)
        
        // --------- ATUALIZAÇÃO DE AURA ---------
        AuraSystem.updateAura(data)
        
        // --------- REGENERAÇÃO DE STAMINA ---------
        data.currentStamina = minOf(
            data.maxStamina,
            data.currentStamina + data.staminaRegenRate * 0.5f
        )
        
        // --------- ESTADO DO KI ---------
        updateKiState(data)
        
        // --------- SINCRONIZAÇÃO ---------
        // (será implementado em NetworkManager)
    }
    
    /**
     * Usa Ki para uma ação específica
     * @return true se teve Ki suficiente e ação foi feita, false caso contrário
     */
    fun useKi(data: PlayerKiData, amount: Float): Boolean {
        if (data.currentKi < amount) {
            return false
        }
        data.currentKi = (data.currentKi - amount).coerceAtLeast(0f)
        return true
    }
    
    /**
     * Restaura Ki manualmente
     */
    fun restoreKi(data: PlayerKiData, amount: Float) {
        data.currentKi = (data.currentKi + amount).coerceAtMost(data.maxKi)
    }
    
    /**
     * Faz jogador voar (consome Ki continuamente)
     * @return false se não tem Ki suficiente
     */
    fun activateFlying(data: PlayerKiData): Boolean {
        val flyCost = 5f  // Ki inicial para começar a voar
        if (data.currentKi < flyCost) {
            return false
        }
        data.currentKi -= flyCost
        data.currentKiState = KiState.FLYING
        return true
    }
    
    /**
     * Tenta fazer dash (movimento rápido)
     * @return false se não tem Ki/stamina suficiente
     */
    fun attemptDash(data: PlayerKiData): Boolean {
        if (data.currentStamina < 20f) {
            return false
        }
        
        if (!KiDrain.drainDash(data)) {
            return false
        }
        
        data.currentStamina -= 20f
        data.currentKiState = KiState.DASHING
        return true
    }
    
    /**
     * Tenta atacar com poder de Ki
     * @param attackPower Escala do ataque (1.0 = normal, 2.0 = dobro)
     * @return false se não tem Ki suficiente
     */
    fun attemptAttack(data: PlayerKiData, attackPower: Float = 1.0f): Boolean {
        return KiDrain.drainAttack(data, attackPower)
    }
    
    /**
     * Começa a carregar Ki (player segurando tecla)
     */
    fun startCharging(data: PlayerKiData) {
        data.isChargingKi = true
        data.currentKiState = KiState.CHARGING
        data.chargeProgress = 0f
    }
    
    /**
     * Stop charging
     * @return Quantidade de Ki carregada (pode ser usada em especial)
     */
    fun stopCharging(data: PlayerKiData): Float {
        val chargedKi = data.chargeProgress * data.maxKi
        data.isChargingKi = false
        data.chargeProgress = 0f
        return chargedKi
    }
    
    /**
     * Atualiza progresso de charge (0.0-1.0)
     */
    fun updateChargeProgress(data: PlayerKiData) {
        if (!data.isChargingKi) return
        
        // Carrega 2% por tick (máx 50 ticks = 1 segundo)
        data.chargeProgress = minOf(1.0f, data.chargeProgress + 0.02f)
        
        // Consome Ki enquanto carrega
        KiDrain.drainCharging(data, 1)
    }
    
    /**
     * Tenta ativar uma transformação
     */
    fun activateTransformation(data: PlayerKiData, newForm: com.bernardo.dragoncraft.core.data.TransformationState): Boolean {
        return TransformationManager.activateTransformation(data, newForm)
    }
    
    /**
     * Deactiva transformação
     */
    fun deactivateTransformation(data: PlayerKiData) {
        TransformationManager.deactivateTransformation(data)
    }
    
    /**
     * Tenta ativar aura
     */
    fun activateAura(data: PlayerKiData) {
        AuraSystem.activateAura(data)
    }
    
    /**
     * Desativa aura
     */
    fun deactivateAura(data: PlayerKiData) {
        AuraSystem.deactivateAura(data)
    }
    
    /**
     * Retorna o multiplicador de poder total do jogador
     */
    fun getTotalPowerMultiplier(data: PlayerKiData): Float {
        var multiplier = data.getTotalPowerMultiplier()
        multiplier *= KiModifiers.applyWeaknessEffect(data)
        return multiplier
    }
    
    /**
     * Retorna o multiplicador de velocidade total
     */
    fun getTotalSpeedMultiplier(data: PlayerKiData): Float {
        return data.getTotalSpeedMultiplier()
    }
    
    /**
     * Valida e corrige dados inválidos
     * (chamado ao carregar player)
     */
    fun validateData(data: PlayerKiData) {
        data.currentKi = data.currentKi.coerceIn(0f, data.maxKi)
        data.currentStamina = data.currentStamina.coerceIn(0f, data.maxStamina)
        data.kiControl = data.kiControl.coerceIn(0.1f, 1.0f)
        data.kiPower = data.kiPower.coerceAtLeast(0.5f)
        data.kiDefense = data.kiDefense.coerceIn(0f, 5f)
    }
    
    /**
     * Atualiza estado de Ki baseado em condições atuais
     */
    private fun updateKiState(data: PlayerKiData) {
        // Atualizar estado crítico
        if (data.currentKi < data.maxKi * 0.2f) {
            data.currentKiState = KiState.LOW_KI
        } else if (data.currentKiState == KiState.LOW_KI) {
            // Sair de LOW_KI quando recuperar
            data.currentKiState = KiState.IDLE
        }
    }
    
    /**
     * Retorna informações de debug do Ki
     */
    fun getDebugInfo(data: PlayerKiData): String {
        return buildString {
            append("=== KI DEBUG ===%n")
            append("Ki: ${String.format("%.1f", data.currentKi)}/${String.format("%.1f", data.maxKi)} (${String.format("%.0f", data.getKiPercent() * 100)}%)%n")
            append("Stamina: ${String.format("%.1f", data.currentStamina)}/${String.format("%.1f", data.maxStamina)}%n")
            append("Estado: ${data.currentKiState}%n")
            append("Transformação: ${data.transformationState}%n")
            append("Poder: x${String.format("%.2f", getTotalPowerMultiplier(data))}%n")
            append("Velocidade: x${String.format("%.2f", getTotalSpeedMultiplier(data))}%n")
            append("Controle: ${String.format("%.0f", data.kiControl * 100)}%%%n")
            append("Defesa: x${String.format("%.2f", data.kiDefense)}%n")
            append("Aura: ${if (data.auraActive) "Ativa (${String.format("%.1f", data.auraIntensity * 100)}%)" else "Inativa"}%n")
        }
    }
}