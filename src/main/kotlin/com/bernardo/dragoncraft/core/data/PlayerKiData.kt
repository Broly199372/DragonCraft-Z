package com.bernardo.dragoncraft.core.data

import net.minecraft.nbt.NbtCompound

/**
 * Armazena TODOS os dados de Ki de um jogador.
 * Funciona como o modelo de dados central do sistema.
 * 
 * Seus dados:
 * - Persistem ao relog (salvo em NBT)
 * - Sincronizam entre cliente/servidor
 * - Sobrevivem a respawn (com opção de perda)
 */
data class PlayerKiData(
    // --- KI E STAMINA ---
    var currentKi: Float = 100f,
    var maxKi: Float = 100f,
    var currentStamina: Float = 100f,
    var maxStamina: Float = 100f,
    
    // --- ATRIBUTOS DE PODER ---
    var kiControl: Float = 1.0f,         // Quanto % do poder você consegue usar (0.0-1.0)
    var kiPower: Float = 1.0f,           // Multiplicador de potência dos ataques
    var kiDefense: Float = 1.0f,         // Multiplicador de defesa/armadura
    
    // --- TAXAS DE MUDANÇA ---
    var kiRegenRate: Float = 2.0f,       // Ki por tick regenerado
    var kiDrainRate: Float = 1.5f,       // Ki por tick drenado (base)
    var staminaRegenRate: Float = 1.5f,  // Stamina por tick regenerada
    
    // --- ESTADO DO GAMER ---
    var race: RaceType = RaceType.HUMAN,
    var transformationState: TransformationState = TransformationState.NONE,
    var currentKiState: KiState = KiState.IDLE,
    var isChargingKi: Boolean = false,
    var chargeProgress: Float = 0f,      // 0.0-1.0
    
    // --- AURA ---
    var auraActive: Boolean = false,
    var auraIntensity: Float = 0f,       // Intensidade da aura (0.0-1.0)
    
    // --- PENALTY/COOLDOWN ---
    var damageNerfTicks: Int = 0,        // Ticks de penalidade por dano recente
    var lastAttackTime: Long = 0L,       // Último time de ataque
    var consecutiveActionsCount: Int = 0 // Ações consecutivas (rápidas)
) {
    
    // --- MÉTODOS UTILITÁRIOS ---
    
    /**
     * Retorna o Ki atual em porcentagem (0-1)
     */
    fun getKiPercent(): Float {
        if (maxKi <= 0f) return 0f
        return (currentKi / maxKi).coerceIn(0f, 1f)
    }
    
    /**
     * Retorna a stamina em porcentagem (0-1)
     */
    fun getStaminaPercent(): Float {
        if (maxStamina <= 0f) return 0f
        return (currentStamina / maxStamina).coerceIn(0f, 1f)
    }
    
    /**
     * Verifica se tem Ki suficiente para uma ação
     */
    fun hasEnoughKi(amount: Float): Boolean {
        return currentKi >= amount
    }
    
    /**
     * Verifica se tem stamina suficiente
     */
    fun hasEnoughStamina(amount: Float): Boolean {
        return currentStamina >= amount
    }
    
    /**
     * Retorna o multiplicador de poder total com transformação
     */
    fun getTotalPowerMultiplier(): Float {
        var multiplier = kiPower * kiControl
        if (transformationState.isActive()) {
            multiplier *= transformationState.powerMultiplier
        }
        return multiplier
    }
    
    /**
     * Retorna o multiplicador de velocidade com transformação
     */
    fun getTotalSpeedMultiplier(): Float {
        var multiplier = 1.0f
        if (transformationState.isActive()) {
            multiplier = transformationState.speedMultiplier
        }
        return multiplier
    }
    
    /**
     * Cria uma cópia profunda dos dados
     */
    fun copy(): PlayerKiData = this.copy()
    
    // --- PERSISTÊNCIA EM NBT ---
    
    /**
     * Salva todos os dados no mundo (arquivo de save)
     */
    fun writeToNbt(nbt: NbtCompound) {
        nbt.putFloat("CurrentKi", currentKi)
        nbt.putFloat("MaxKi", maxKi)
        nbt.putFloat("CurrentStamina", currentStamina)
        nbt.putFloat("MaxStamina", maxStamina)
        
        nbt.putFloat("KiControl", kiControl)
        nbt.putFloat("KiPower", kiPower)
        nbt.putFloat("KiDefense", kiDefense)
        
        nbt.putFloat("KiRegenRate", kiRegenRate)
        nbt.putFloat("KiDrainRate", kiDrainRate)
        nbt.putFloat("StaminaRegenRate", staminaRegenRate)
        
        nbt.putString("Race", race.name)
        nbt.putString("Transformation", transformationState.name)
        nbt.putString("KiState", currentKiState.name)
        nbt.putBoolean("IsCharging", isChargingKi)
        nbt.putFloat("ChargeProgress", chargeProgress)
        
        nbt.putBoolean("AuraActive", auraActive)
        nbt.putFloat("AuraIntensity", auraIntensity)
        
        nbt.putInt("DamageNerfTicks", damageNerfTicks)
        nbt.putLong("LastAttackTime", lastAttackTime)
        nbt.putInt("ConsecutiveActions", consecutiveActionsCount)
    }
    
    /**
     * Carrega dados do mundo ao fazer login
     */
    fun readFromNbt(nbt: NbtCompound) {
        if (nbt.contains("CurrentKi")) currentKi = nbt.getFloat("CurrentKi")
        if (nbt.contains("MaxKi")) maxKi = nbt.getFloat("MaxKi")
        if (nbt.contains("CurrentStamina")) currentStamina = nbt.getFloat("CurrentStamina")
        if (nbt.contains("MaxStamina")) maxStamina = nbt.getFloat("MaxStamina")
        
        if (nbt.contains("KiControl")) kiControl = nbt.getFloat("KiControl")
        if (nbt.contains("KiPower")) kiPower = nbt.getFloat("KiPower")
        if (nbt.contains("KiDefense")) kiDefense = nbt.getFloat("KiDefense")
        
        if (nbt.contains("KiRegenRate")) kiRegenRate = nbt.getFloat("KiRegenRate")
        if (nbt.contains("KiDrainRate")) kiDrainRate = nbt.getFloat("KiDrainRate")
        if (nbt.contains("StaminaRegenRate")) staminaRegenRate = nbt.getFloat("StaminaRegenRate")
        
        if (nbt.contains("Race")) race = RaceType.valueOf(nbt.getString("Race"))
        if (nbt.contains("Transformation")) transformationState = TransformationState.valueOf(nbt.getString("Transformation"))
        if (nbt.contains("KiState")) currentKiState = KiState.valueOf(nbt.getString("KiState"))
        if (nbt.contains("IsCharging")) isChargingKi = nbt.getBoolean("IsCharging")
        if (nbt.contains("ChargeProgress")) chargeProgress = nbt.getFloat("ChargeProgress")
        
        if (nbt.contains("AuraActive")) auraActive = nbt.getBoolean("AuraActive")
        if (nbt.contains("AuraIntensity")) auraIntensity = nbt.getFloat("AuraIntensity")
        
        if (nbt.contains("DamageNerfTicks")) damageNerfTicks = nbt.getInt("DamageNerfTicks")
        if (nbt.contains("LastAttackTime")) lastAttackTime = nbt.getLong("LastAttackTime")
        if (nbt.contains("ConsecutiveActions")) consecutiveActionsCount = nbt.getInt("ConsecutiveActions")
    }
}
