package com.bernardo.dragoncraft.core

import com.bernardo.dragoncraft.core.data.PlayerKiData
import net.minecraft.entity.player.PlayerEntity
import java.util.*
import kotlin.collections.HashMap

/**
 * KI MANAGER - GERENCIADOR GLOBAL DE KI
 * 
 * Responsável por:
 * - Cache de dados de Ki por jogador (evita recalcular)
 * - Acesso rápido aos dados
 * - Limpeza de cache quando jogador sai
 * - Sincronização inicial ao fazer login
 * 
 * IMPORTANTE: Este é um singleton! Use KiManager.getPlayerKiData(player)
 */
object KiManager {
    
    /** Cache de dados: UUID do player -> PlayerKiData */
    private val playerKiCache = HashMap<UUID, PlayerKiData>()
    
    /** Rastreamento de última sincronização (evita sync spam) */
    private val lastSyncTime = HashMap<UUID, Long>()
    
    /** Intervalo mínimo entre syncs (ms) */
    private const val SYNC_INTERVAL_MS = 100L  // 100ms minimum
    
    /**
     * Obtém os dados de Ki de um jogador (com cache)
     * Se não existem, cria novos dados padrão
     */
    fun getPlayerKiData(player: PlayerEntity): PlayerKiData {
        return playerKiCache.getOrPut(player.uuid) {
            PlayerKiData()  // Criar dados padrão
        }
    }
    
    /**
     * Obtém dados de Ki ou null se não existem
     */
    fun getPlayerKiDataOrNull(uuid: UUID): PlayerKiData? {
        return playerKiCache[uuid]
    }
    
    /**
     * Armazena dados de Ki (override)
     */
    fun setPlayerKiData(player: PlayerEntity, data: PlayerKiData) {
        playerKiCache[player.uuid] = data
    }
    
    /**
     * Remove dados do cache quando jogador sai
     */
    fun onPlayerLogout(uuid: UUID) {
        playerKiCache.remove(uuid)
        lastSyncTime.remove(uuid)
    }
    
    /**
     * Calcula hash dos dados para detectar mudanças
     * (evita enviar packets quando nada mudou)
     */
    fun getDataHash(data: PlayerKiData): Long {
        return (
            data.currentKi.toLong() xor
            data.maxKi.toLong() xor
            data.currentStamina.toLong() xor
            data.transformationState.ordinal.toLong() xor
            data.auraActive.hashCode().toLong() xor
            data.currentKiState.ordinal.toLong()
        )
    }
    
    /**
     * Verifica se passou tempo suficiente desde última sync
     * (para não spammar packets)
     */
    fun shouldSync(player: PlayerEntity): Boolean {
        val now = System.currentTimeMillis()
        val last = lastSyncTime.getOrDefault(player.uuid, 0L)
        
        return now - last >= SYNC_INTERVAL_MS
    }
    
    /**
     * Marca que sincronizou agora
     */
    fun recordSync(player: PlayerEntity) {
        lastSyncTime[player.uuid] = System.currentTimeMillis()
    }
    
    /**
     * Limpa todo o cache (útil em teste ou reload)
     */
    fun clearCache() {
        playerKiCache.clear()
        lastSyncTime.clear()
    }
    
    /**
     * Retorna número de players com dados em cache
     */
    fun getCacheSize(): Int {
        return playerKiCache.size
    }
    
    /**
     * Debug: lista todos os players em cache
     */
    fun getDebugInfo(): String {
        return buildString {
            append("=== KI MANAGER DEBUG ===%n")
            append("Jogadores em cache: ${playerKiCache.size}%n")
            playerKiCache.forEach { (uuid, data) ->
                append("- $uuid: Ki=${String.format("%.0f", data.currentKi)}/${String.format("%.0f", data.maxKi)}%n")
            }
        }
    }
}
