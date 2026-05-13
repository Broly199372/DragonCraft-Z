package com.bernardo.dragoncraft.core.data

/**
 * Representa transformações do jogador em estilo anime.
 * Cada transformação tem multiplicadores, drain e efeitos próprios.
 */
enum class TransformationState(
    val displayName: String,
    val powerMultiplier: Float,        // Multiplicador de poder/ataque
    val speedMultiplier: Float,        // Multiplicador de velocidade
    val drainMultiplier: Float,        // Multiplicador de consumo de Ki
    val regenMultiplier: Float,        // Multiplicador de regeneração (durante transformação)
    val color: Int = 0xFFFFFF          // Cor da aura em hex
) {
    /** Estado padrão sem transformação */
    NONE("Normal", 1.0f, 1.0f, 1.0f, 1.0f, 0xFFFFFF),
    
    // Transformações base (expansível)
    POWER_UP("Power Up", 1.5f, 1.2f, 1.3f, 0.7f, 0xFFD700),
    SUPER_FORM("Super Form", 2.5f, 1.5f, 2.0f, 0.5f, 0xFF8C00),
    ULTIMATE_POWER("Ultimate Power", 4.0f, 2.0f, 3.5f, 0.3f, 0xFF0000);
    
    /**
     * Retorna o drain total para esta transformação (multiplicador * base)
     */
    fun getTotalDrainMultiplier(): Float = drainMultiplier
    
    /**
     * Verifica se esta transformação está ativa (não é NONE)
     */
    fun isActive(): Boolean = this != NONE
    
    companion object {
        /** Retorna a transformação anterior na progressão */
        fun getPreviousTransformation(current: TransformationState): TransformationState {
            return when (current) {
                NONE -> NONE
                POWER_UP -> NONE
                SUPER_FORM -> POWER_UP
                ULTIMATE_POWER -> SUPER_FORM
            }
        }
        
        /** Retorna a próxima transformação na progressão */
        fun getNextTransformation(current: TransformationState): TransformationState {
            return when (current) {
                NONE -> POWER_UP
                POWER_UP -> SUPER_FORM
                SUPER_FORM -> ULTIMATE_POWER
                ULTIMATE_POWER -> ULTIMATE_POWER  // Máximo alcançado
            }
        }
    }
}
