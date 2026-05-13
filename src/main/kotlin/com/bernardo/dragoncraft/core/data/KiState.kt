package com.bernardo.dragoncraft.core.data

/**
 * Representa o estado atual do Ki do jogador.
 * Usado para determinar comportamentos de regen, drain e efeitos visuais.
 */
enum class KiState {
    /** Jogador parado e regenerando normalmente */
    IDLE,
    
    /** Jogador se movendo (caminhando/correndo) */
    MOVING,
    
    /** Jogador carregando Ki (apertando tecla de charge) */
    CHARGING,
    
    /** Jogador usando uma habilidade/ataque */
    ATTACKING,
    
    /** Jogador voando */
    FLYING,
    
    /** Jogador em dash/movimento rápido */
    DASHING,
    
    /** Jogador com transformação ativa */
    TRANSFORMED,
    
    /** Jogador com pouco Ki (crítico) */
    LOW_KI;
    
    /**
     * Retorna se este estado usa energia continuamente
     */
    fun drainsContinuously(): Boolean = this in setOf(FLYING, TRANSFORMED, CHARGING)
    
    /**
     * Retorna o multiplicador de regeneração para este estado
     */
    fun regenMultiplier(): Float = when (this) {
        IDLE -> 1.5f        // Parado regen mais
        MOVING -> 0.8f      // Movendo regen menos
        CHARGING -> 0.0f    // Carregando não regen
        ATTACKING -> 0.2f   // Atacando muito pouco
        FLYING -> 0.3f      // Voando pouco
        DASHING -> 0.0f     // Dash não regen
        TRANSFORMED -> 0.5f // Transformado regen reduzido
        LOW_KI -> 1.2f      // Baixo Ki regen ligeiramente aumentado
    }
}
