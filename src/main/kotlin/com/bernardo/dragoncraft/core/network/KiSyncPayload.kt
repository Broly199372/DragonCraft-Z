package com.bernardo.dragoncraft.core.network

import com.bernardo.dragoncraft.core.data.PlayerKiData
import net.minecraft.network.PacketByteBuf

object KiSyncPayload {

    fun write(buffer: PacketByteBuf, data: PlayerKiData) {
        buffer.writeFloat(data.currentKi)
        buffer.writeFloat(data.maxKi)
        buffer.writeFloat(data.currentStamina)
        buffer.writeFloat(data.maxStamina)
        buffer.writeFloat(data.kiControl)
        buffer.writeFloat(data.kiPower)
        buffer.writeFloat(data.kiDefense)
        buffer.writeFloat(data.kiRegenRate)
        buffer.writeFloat(data.kiDrainRate)
        buffer.writeFloat(data.staminaRegenRate)
        buffer.writeString(data.race.name)
        buffer.writeString(data.transformationState.name)
        buffer.writeString(data.currentKiState.name)
        buffer.writeBoolean(data.isChargingKi)
        buffer.writeFloat(data.chargeProgress)
        buffer.writeBoolean(data.auraActive)
        buffer.writeFloat(data.auraIntensity)
        buffer.writeInt(data.damageNerfTicks)
        buffer.writeLong(data.lastAttackTime)
        buffer.writeInt(data.consecutiveActionsCount)
    }

    fun read(buffer: PacketByteBuf, data: PlayerKiData) {
        data.currentKi = buffer.readFloat()
        data.maxKi = buffer.readFloat()
        data.currentStamina = buffer.readFloat()
        data.maxStamina = buffer.readFloat()
        data.kiControl = buffer.readFloat()
        data.kiPower = buffer.readFloat()
        data.kiDefense = buffer.readFloat()
        data.kiRegenRate = buffer.readFloat()
        data.kiDrainRate = buffer.readFloat()
        data.staminaRegenRate = buffer.readFloat()
        data.race = com.bernardo.dragoncraft.core.data.RaceType.valueOf(buffer.readString(32767))
        data.transformationState = com.bernardo.dragoncraft.core.data.TransformationState.valueOf(buffer.readString(32767))
        data.currentKiState = com.bernardo.dragoncraft.core.data.KiState.valueOf(buffer.readString(32767))
        data.isChargingKi = buffer.readBoolean()
        data.chargeProgress = buffer.readFloat()
        data.auraActive = buffer.readBoolean()
        data.auraIntensity = buffer.readFloat()
        data.damageNerfTicks = buffer.readInt()
        data.lastAttackTime = buffer.readLong()
        data.consecutiveActionsCount = buffer.readInt()
    }
}
