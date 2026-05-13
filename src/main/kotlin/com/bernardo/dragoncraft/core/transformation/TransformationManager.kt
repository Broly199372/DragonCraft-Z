package com.bernardo.dragoncraft.core.transformation

import com.bernardo.dragoncraft.core.data.PlayerKiData
import com.bernardo.dragoncraft.core.data.TransformationState
import com.bernardo.dragoncraft.core.mechanics.KiDrain

/**
 * Gerenciador de Transformações.
 * 
 * Permite ativar/desativar transformações com validações de Ki.
 * Suporta transições entre formas.
 * Implementação é genérica e expansível para novas transformações.
 */
object TransformationManager {
    
    /**
     * Tenta ativar uma transformação
     * @return true se conseguiu, false caso contrário
     */
    fun activateTransformation(data: PlayerKiData, newTransformation: TransformationState): Boolean {
        // Não pode transformar se já está transformado
        if (data.transformationState.isActive() && data.transformationState != newTransformation) {
            // Sim, pode fazer transição (ex: POWER_UP -> SUPER_FORM)
            return transitionTransformation(data, newTransformation)
        }
        
        if (data.transformationState == newTransformation) {
            return true  // Já está nessa forma
        }
        
        // Validar Ki suficiente
        val requiredKi = getKiRequirement(newTransformation)
        if (data.currentKi < requiredKi) {
            return false
        }
        
        // Consome Ki inicial
        data.currentKi -= requiredKi
        
        // Ativa transformação
        data.transformationState = newTransformation
        return true
    }
    
    /**
     * Desativa transformação (volta ao normal)
     */
    fun deactivateTransformation(data: PlayerKiData): Boolean {
        if (data.transformationState == TransformationState.NONE) {
            return false  // Já não está transformado
        }
        
        data.transformationState = TransformationState.NONE
        return true
    }
    
    /**
     * Faz transição entre transformações (ex: Power Up -> Super Form)
     */
    private fun transitionTransformation(data: PlayerKiData, newTransformation: TransformationState): Boolean {
        // Se pode descer (Ex: SUPER_FORM -> POWER_UP, sempre permite)
        val previousForm = TransformationState.getPreviousTransformation(data.transformationState)
        if (newTransformation == previousForm) {
            data.transformationState = newTransformation
            return true
        }
        
        // Se pode subir (Ex: POWER_UP -> SUPER_FORM, precisa de Ki)
        val nextForm = TransformationState.getNextTransformation(data.transformationState)
        if (newTransformation == nextForm) {
            val requiredKi = getKiRequirement(newTransformation)
            if (data.currentKi >= requiredKi) {
                data.currentKi -= requiredKi
                data.transformationState = newTransformation
                return true
            }
            return false
        }
        
        return false
    }
    
    /**
     * Retorna quanto Ki é necessário para ativar transformação
     */
    fun getKiRequirement(transformation: TransformationState): Float {
        return when (transformation) {
            TransformationState.NONE -> 0f
            TransformationState.POWER_UP -> 25f
            TransformationState.SUPER_FORM -> 50f
            TransformationState.ULTIMATE_POWER -> 100f
        }
    }
    
    /**
     * Verifica se pode ativar uma transformação
     */
    fun canActivate(data: PlayerKiData, transformation: TransformationState): Boolean {
        val required = getKiRequirement(transformation)
        return data.currentKi >= required
    }
    
    /**
     * Processa cada tick enquanto transformado
     * (drain contínuo, verificações, etc)
     */
    fun updateTransformation(data: PlayerKiData, currentTick: Long) {
        if (!data.transformationState.isActive()) return
        
        // Drena Ki continuamente
        KiDrain.drainTransformation(data)
        
        // Se ficou sem Ki enquanto transformado, deactivar
        if (data.currentKi <= 0f) {
            deactivateTransformation(data)
        }
    }
    
    /**
     * Retorna descrição da transformação para HUD/debug
     */
    fun getTransformationInfo(transformation: TransformationState): String {
        return buildString {
            append(transformation.displayName)
            append(" | Poder: x${transformation.powerMultiplier}")
            append(" | Velocidade: x${transformation.speedMultiplier}")
            append(" | Drain: x${transformation.drainMultiplier}")
        }
    }
}
