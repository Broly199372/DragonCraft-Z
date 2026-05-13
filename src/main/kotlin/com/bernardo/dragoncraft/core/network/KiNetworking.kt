package com.bernardo.dragoncraft.core.network

import com.bernardo.dragoncraft.core.KiManager
import com.bernardo.dragoncraft.core.data.PlayerKiData
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

/**
 * Gerencia o canal de sincronização entre servidor e cliente.
 * Evita overhead e packets grandes, sincronizando apenas o necessário.
 */
object KiNetworking {
    val KI_SYNC_ID = Identifier("dragoncraft", "ki_sync")

    fun registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(KI_SYNC_ID) { _, _, _, _, _ ->
            // O servidor não processa sync de Ki do cliente neste sistema.
        }
    }

    fun registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(KI_SYNC_ID) { client, _, buffer, _ ->
            client.execute {
                val localPlayer = MinecraftClient.getInstance().player ?: return@execute
                val data = KiManager.getPlayerKiData(localPlayer)
                KiSyncPayload.read(buffer, data)
            }
        }
    }

    fun syncPlayerKiToClient(player: ServerPlayerEntity, data: PlayerKiData) {
        val buffer = PacketByteBufs.create()
        KiSyncPayload.write(buffer, data)
        ServerPlayNetworking.send(player, KI_SYNC_ID, buffer)
    }

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
