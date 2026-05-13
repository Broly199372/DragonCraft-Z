package com.bernardo.dragoncraft.core.data

/**
 * Define diferentes raças com bônus e malus de Ki.
 * Cada raça tem características únicas de regeneração e consumo.
 */
enum class RaceType(
    val displayName: String,
    val baseKi: Float,           // Ki máximo base
    val regenBonus: Float,       // Bônus de regeneração (%)
    val drainPenalty: Float,     // Penalidade de drenagem (%)
    val transformationBonus: Float // Bônus em transformações
) {
    /** Humano equilibrado */
    HUMAN("Humano", 100f, 0f, 0f, 0f),
    
    /** Guerreiro poderoso mas come mais Ki */
    SAIYAN("Saiyajin", 120f, 1.1f, 1.2f, 1.3f),
    
    /** Raça espiritual, regen rápida */
    DEMON("Demônio", 110f, 1.3f, 0.9f, 1.1f),
    
    /** Inteligente mas fraco */
    ANDROID("Android", 130f, 0.5f, 0.8f, 0.9f);
    
    /**
     * Calcula o Ki máximo com bônus da raça
     */
    fun calculateMaxKi(baseBonus: Float = 0f): Float {
        return baseKi + baseBonus
    }
}
