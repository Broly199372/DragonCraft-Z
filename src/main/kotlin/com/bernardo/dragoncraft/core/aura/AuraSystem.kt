package com.bernardo.dragoncraft.core.aura

import com.bernardo.dragoncraft.core.data.PlayerKiData
import kotlin.math.max
import kotlin.math.min

/**
 * Sistema de Aura Visual.
 * 
 * Gerencia quando a aura é ativada, sua intensidade e efeitos.
 * Completamente desacoplado de lógica de combate.
 * Suporta renderização no cliente sem impacto no servidor.
 */
object AuraSystem {
    
    /** Intensidade máxima (1.0) */
    private const val MAX_INTENSITY = 1.0f
    
    /** Intensidade mínima visível (0.2) */
    private const val MIN_INTENSITY_VISIBLE = 0.2f
    
    /**
     * Ativa a aura do jogador
     */
    fun activateAura(data: PlayerKiData): Boolean {
        if (data.auraActive) return true  // Já está ativa
        
        data.auraActive = true
        data.auraIntensity = 0.5f  // Começa em 50%
        return true
    }
    
    /**
     * Desativa a aura do jogador
     */
    fun deactivateAura(data: PlayerKiData): Boolean {
        if (!data.auraActive) return false
        
        data.auraActive = false
        data.auraIntensity = 0f
        return true
    }
    
    /**
     * Aumenta a intensidade da aura gradualmente
     * (enquanto carrega ou em combate intenso)
     */
    fun increaseIntensity(data: PlayerKiData, amount: Float = 0.05f) {
        if (!data.auraActive) {
            activateAura(data)
        }
        
        val kiPercent = data.getKiPercent()
        val maxIntensity = min(MAX_INTENSITY, kiPercent)
        
        data.auraIntensity = min(maxIntensity, data.auraIntensity + amount)
    }
    
    /**
     * Diminui a intensidade da aura gradualmente
     */
    fun decreaseIntensity(data: PlayerKiData, amount: Float = 0.05f) {
        data.auraIntensity = max(0f, data.auraIntensity - amount)
        
        // Desativa se intensidade muito baixa
        if (data.auraIntensity <= 0.1f) {
            deactivateAura(data)
        }
    }
    
    /**
     * Atualiza aura baseado no estado do jogador (cada tick)
     */
    fun updateAura(data: PlayerKiData) {
        if (!data.auraActive) return
        
        // Aura intensidade segue o % de Ki
        val kiPercent = data.getKiPercent()
        
        // Transição suave entre intensidades
        val targetIntensity = kiPercent * MAX_INTENSITY
        
        // Lerp (interpolação linear) para suavidade
        data.auraIntensity = lerp(data.auraIntensity, targetIntensity, 0.1f)
        
        // Se transformado, aumentar intensidade automática
        if (data.transformationState.isActive()) {
            data.auraIntensity = min(MAX_INTENSITY, data.auraIntensity + 0.02f)
        }
    }
    
    /**
     * Retorna a cor da aura baseada em transformação
     */
    fun getAuraColor(data: PlayerKiData): Int {
        return data.transformationState.color
    }
    
    /**
     * Retorna tamanho/raio da aura
     * Maior quanto mais KI tem
     */
    fun getAuraRadius(data: PlayerKiData): Float {
        val baseRadius = 1.5f
        val intensityBonus = data.auraIntensity * 0.5f
        val kiBonus = data.getKiPercent() * 0.3f
        
        return baseRadius + intensityBonus + kiBonus
    }
    
    /**
     * Retorna quantas camadas de aura renderizar
     * (camadas = mais visual legal)
     */
    fun getAuraLayers(data: PlayerKiData): Int {
        return when {
            data.auraIntensity > 0.8f -> 3
            data.auraIntensity > 0.5f -> 2
            data.auraIntensity > 0.2f -> 1
            else -> 0
        }
    }
    
    /**
     * Retorna opacidade da aura (0.0-1.0)
     */
    fun getAuraOpacity(data: PlayerKiData): Float {
        if (!data.auraActive) return 0f
        
        val baseOpacity = data.auraIntensity
        
        // Se em transformação, aura mais visível
        if (data.transformationState.isActive()) {
            return min(1f, baseOpacity * 1.3f)
        }
        
        return baseOpacity
    }
    
    /**
     * Retorna velocidade de rotação da aura (em graus por tick)
     * Mais rápida quando tem mais Ki
     */
    fun getAuraRotationSpeed(data: PlayerKiData): Float {
        val baseSpeed = 0.5f
        val kiMultiplier = data.getKiPercent() * 0.5f
        
        return baseSpeed + kiMultiplier
    }
    
    /**
     * Simples lerp helper
     */
    private fun lerp(start: Float, end: Float, t: Float): Float {
        return start + (end - start) * t.coerceIn(0f, 1f)
    }
}
