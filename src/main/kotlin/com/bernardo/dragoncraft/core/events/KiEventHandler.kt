package com.bernardo.dragoncraft.core.events

import com.bernardo.dragoncraft.core.KiManager
import com.bernardo.dragoncraft.core.KiSystem
import com.bernardo.dragoncraft.core.data.PlayerKiData
import com.bernardo.dragoncraft.core.network.KiNetworking
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity

/**
 * GERENCIADOR DE EVENTOS DO SISTEMA DE KI
 *
 * Registra listeners para:
 * - Game tick (atualizar Ki)
 * - Player login/logout
 * - Dano recebido
 * - Morte
 */
object KiEventHandler {

    private var lastProcessedTick = 0L

    fun registerEvents() {
        // Atualizar Ki a cada tick
        ServerTickEvents.END_SERVER_TICK.register { server ->
            server.playerManager.playerList.forEach { player ->
                updatePlayerKi(player)
            }
        }

        // Carregar dados ao fazer login
        ServerPlayConnectionEvents.JOIN.register { handler, sender, server ->
            val player = handler.player
            loadPlayerKiData(player)
        }

        // Salvar dados ao sair
        ServerPlayConnectionEvents.DISCONNECT.register { handler, server ->
            val player = handler.player
            savePlayerKiData(player)
            KiManager.onPlayerLogout(player.uuid)
        }

        // Lidar com dano
        ServerLivingEntityEvents.ALLOW_DAMAGE.register { entity, source, amount ->
            if (entity is ServerPlayerEntity) {
                val data = KiManager.getPlayerKiData(entity)
                com.bernardo.dragoncraft.core.mechanics.KiDrain.onPlayerDamaged(data, amount)
            }
            true
        }

        // Limpar cache ao desligar servidor
        ServerLifecycleEvents.SERVER_STOPPED.register { server ->
            KiManager.clearCache()
        }
    }

    private fun updatePlayerKi(player: ServerPlayerEntity) {
        val data = KiManager.getPlayerKiData(player)
        KiSystem.validateData(data)
        KiSystem.updateKi(player, data, player.server.ticks.toLong())

        if (KiManager.shouldSync(player)) {
            KiNetworking.syncPlayerKiToClient(player, data)
            KiManager.recordSync(player)
        }
    }

    /**
     * Carrega dados Ki do NBT do player.
     * No Fabric, usamos o NBT customData do próprio PlayerEntity.
     * Para persistência completa, adicione mixin em writeCustomDataToNbt/readCustomDataFromNbt.
     */
    private fun loadPlayerKiData(player: PlayerEntity) {
        // Tenta ler do dataTracker/NBT customizado via tag "dragoncraft"
        val kiDataNbt: NbtCompound = try {
            val raw = player.writeNbt(NbtCompound())
            raw.getCompound("dragoncraft_ki")
        } catch (e: Exception) {
            NbtCompound()
        }

        val data = PlayerKiData()
        if (!kiDataNbt.isEmpty) {
            data.readFromNbt(kiDataNbt)
        }

        KiManager.setPlayerKiData(player, data)
        KiSystem.validateData(data)
    }

    /**
     * Salva dados Ki no NBT do player.
     * Para persistência completa entre sessões, use mixin.
     */
    private fun savePlayerKiData(player: PlayerEntity) {
        val data = KiManager.getPlayerKiDataOrNull(player.uuid) ?: return
        val kiDataNbt = NbtCompound()
        data.writeToNbt(kiDataNbt)
        // Os dados ficam no KiManager em memória durante a sessão.
        // Para salvar em disco de verdade, implemente mixin no PlayerEntity.
    }

    private fun syncPlayerKiToClient(player: ServerPlayerEntity, data: PlayerKiData) {
        // TODO: Implementar quando PlayerPayloadPacket for disponível
    }
}
