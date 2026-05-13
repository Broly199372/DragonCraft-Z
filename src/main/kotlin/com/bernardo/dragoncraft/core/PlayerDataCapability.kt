package com.bernardo.dragoncraft.core

import net.minecraft.nbt.NbtCompound

class PlayerDataCapability {
    // --- STATUS DO DRAGONCRAFT ---
    var ki: Float = 100.0f
    var maxKi: Float = 100.0f
    
    var stamina: Float = 100.0f
    var maxStamina: Float = 100.0f
    
    // --- MODIFICADORES DO MINECRAFT VANILLA ---
    var bonusHealth: Float = 0.0f      // Vida a mais (ex: +20.0f para dobrar a vida base)
    var jumpHeight: Float = 1.0f       // Multiplicador de pulo (1.0 = normal, 2.0 = dobro)
    var bonusResistance: Float = 0.0f  // Redução de dano / Armadura extra

    /**
     * Salva todos os dados deste jogador no mundo (no arquivo do save).
     * Isso impede que o jogador perca o Ki ou a vida extra ao deslogar.
     */
    fun writeToNbt(nbt: NbtCompound) {
        nbt.putFloat("ki", ki)
        nbt.putFloat("maxKi", maxKi)
        nbt.putFloat("stamina", stamina)
        nbt.putFloat("maxStamina", maxStamina)
        nbt.putFloat("bonusHealth", bonusHealth)
        nbt.putFloat("jumpHeight", jumpHeight)
        nbt.putFloat("bonusResistance", bonusResistance)
    }

    /**
     * Carrega os dados quando o jogador entra no mundo.
     */
    fun readFromNbt(nbt: NbtCompound) {
        if (nbt.contains("ki")) ki = nbt.getFloat("ki")
        if (nbt.contains("maxKi")) maxKi = nbt.getFloat("maxKi")
        if (nbt.contains("stamina")) stamina = nbt.getFloat("stamina")
        if (nbt.contains("maxStamina")) maxStamina = nbt.getFloat("maxStamina")
        if (nbt.contains("bonusHealth")) bonusHealth = nbt.getFloat("bonusHealth")
        if (nbt.contains("jumpHeight")) jumpHeight = nbt.getFloat("jumpHeight")
        if (nbt.contains("bonusResistance")) bonusResistance = nbt.getFloat("bonusResistance")
    }
}
