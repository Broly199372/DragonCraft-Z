package com.bernardo.dragoncraft.core.examples

import com.bernardo.dragoncraft.core.KiManager
import com.bernardo.dragoncraft.core.KiSystem
import com.bernardo.dragoncraft.core.aura.AuraSystem
import com.bernardo.dragoncraft.core.data.KiState
import com.bernardo.dragoncraft.core.data.TransformationState
import com.bernardo.dragoncraft.core.mechanics.KiDrain
import com.bernardo.dragoncraft.core.mechanics.KiModifiers
import com.bernardo.dragoncraft.core.mechanics.KiRegeneration
import com.bernardo.dragoncraft.core.transformation.TransformationManager
import net.minecraft.entity.player.PlayerEntity

/**
 * EXEMPLOS DE USO DO SISTEMA DE KI
 * 
 * Mostra como usar o sistema em cenários reais de gameplay.
 * Copie e adapte estes exemplos para suas habilidades/eventos.
 */

/** === EXEMPLO 1: Implementar Ataque Básico === */
fun playerAttack(player: PlayerEntity, power: Float = 1.0f) {
    val data = KiManager.getPlayerKiData(player)
    
    // Tentar usar Ki para o ataque
    if (!KiSystem.attemptAttack(data, power)) {
        player.sendMessage(
            net.minecraft.text.Text.literal("§cSem Ki suficiente para atacar!"),
            true  // action bar
        )
        return
    }
    
    // Se chegou aqui, o ataque foi feito
    val totalPower = KiSystem.getTotalPowerMultiplier(data)
    val damageDealt = 5f * power * totalPower
    
    player.sendMessage(
        net.minecraft.text.Text.literal("§6Ataque! Dano: $damageDealt"),
        true
    )
}

/** === EXEMPLO 2: Sistema de Voo === */
fun playerStartFlying(player: PlayerEntity) {
    val data = KiManager.getPlayerKiData(player)
    
    if (!KiSystem.activateFlying(data)) {
        player.sendMessage(
            net.minecraft.text.Text.literal("§cNão tem Ki suficiente para voar!"),
            true
        )
        return
    }
    
    player.abilities.allowFlying = true
    player.sendAbilitiesUpdate()
    player.sendMessage(
        net.minecraft.text.Text.literal("§bVoo ativado!"),
        true
    )
}

fun playerStopFlying(player: PlayerEntity) {
    player.abilities.allowFlying = false
    player.sendAbilitiesUpdate()
}

/** === EXEMPLO 3: Dash Rápido === */
fun playerDash(player: PlayerEntity) {
    val data = KiManager.getPlayerKiData(player)
    
    if (!KiSystem.attemptDash(data)) {
        player.sendMessage(
            net.minecraft.text.Text.literal("§cSem Ki/Stamina para dash!"),
            true
        )
        return
    }
    
    // Aplicar velocidade
    val speedMultiplier = KiSystem.getTotalSpeedMultiplier(data)
    player.velocity = player.velocity.multiply(speedMultiplier.toDouble())
    
    player.sendMessage(net.minecraft.text.Text.literal("§eDASH!"), true)
}

/** === EXEMPLO 4: Carregamento de Energia === */
fun playerStartCharging(player: PlayerEntity) {
    val data = KiManager.getPlayerKiData(player)
    KiSystem.startCharging(data)
    player.sendMessage(net.minecraft.text.Text.literal("§6Carregando..."), true)
}

fun playerStopCharging(player: PlayerEntity): Float {
    val data = KiManager.getPlayerKiData(player)
    
    if (!data.isChargingKi) {
        return 0f
    }
    
    val chargedKi = KiSystem.stopCharging(data)
    player.sendMessage(
        net.minecraft.text.Text.literal("§bCarregado: ${chargedKi.toInt()} Ki"),
        true
    )
    
    return chargedKi
}

/** === EXEMPLO 5: Transformações === */
fun playerTransform(player: PlayerEntity, targetForm: TransformationState) {
    val data = KiManager.getPlayerKiData(player)
    
    if (!KiSystem.activateTransformation(data, targetForm)) {
        player.sendMessage(
            net.minecraft.text.Text.literal("§cNão pode transformar! Ki insuficiente ou erro."),
            true
        )
        return
    }
    
    // Efeitos especiais
    AuraSystem.activateAura(data)
    
    player.sendMessage(
        net.minecraft.text.Text.literal("§d${targetForm.displayName} ATIVADA!"),
        false
    )
    
    // Spawnар partículas (exemplo)
    spawnTransformationParticles(player)
}

fun playerDeTransform(player: PlayerEntity) {
    val data = KiManager.getPlayerKiData(player)
    KiSystem.deactivateTransformation(data)
    AuraSystem.deactivateAura(data)
    
    player.sendMessage(
        net.minecraft.text.Text.literal("§7Transformação desativada."),
        true
    )
}

/** === EXEMPLO 6: Habilidade Especial (Genki Dama) === */
fun playerUseSpecialAttack(player: PlayerEntity, attackType: String) {
    val data = KiManager.getPlayerKiData(player)
    
    val kiCost = when (attackType) {
        "GENKI_DAMA" -> 80f
        "KAMEHAMEHA" -> 60f
        "DESTRUCTO_DISK" -> 40f
        else -> 50f
    }
    
    if (!KiSystem.useKi(data, kiCost)) {
        player.sendMessage(
            net.minecraft.text.Text.literal("§cKi insuficiente! Precisa $kiCost, tem ${data.currentKi.toInt()}"),
            true
        )
        return
    }
    
    // Calcular poder total do ataque
    val basePower = 10f
    val multiplier = KiSystem.getTotalPowerMultiplier(data)
    val totalDamage = basePower * multiplier
    
    player.sendMessage(
        net.minecraft.text.Text.literal("§c$attackType! ${String.format("%.1f", totalDamage)} dano!"),
        false
    )
    
    // TODO: Aplicar dano aos inimigos em uma área
}

/** === EXEMPLO 7: Buff de Poder Temporário === */
fun playerGetPowerBuff(player: PlayerEntity, durationSeconds: Int = 10) {
    val data = KiManager.getPlayerKiData(player)
    
    // Aumentar poder em 30%
    KiModifiers.applyPowerBuff(data, 1.3f, durationSeconds * 20)  // 20 ticks = 1 segundo
    
    player.sendMessage(
        net.minecraft.text.Text.literal("§6Poder aumentado por $durationSeconds segundos!"),
        true
    )
}

/** === EXEMPLO 8: Obter Info de Debug === */
fun playerGetKiDebug(player: PlayerEntity) {
    val data = KiManager.getPlayerKiData(player)
    
    val debugInfo = KiSystem.getDebugInfo(data)
    player.sendMessage(net.minecraft.text.Text.literal(debugInfo), false)
    
    val managerInfo = KiManager.getDebugInfo()
    player.sendMessage(net.minecraft.text.Text.literal(managerInfo), false)
}

// Helper functions
private fun spawnTransformationParticles(player: PlayerEntity) {
    // TODO: Usar particle system do Minecraft
    // player.world.addParticle(...)
}

/**
 * === INTEGRAÇÃO EM EVENTO DE TECLA ===
 * 
 * Para usar estas funções, registre listeners para teclas.
 * Exemplo com Fabric API:
 * 
 * ```
 * KeyBindingHelper.registerKeyBinding(keyBinding)
 * 
 * ClientTickEvents.END_CLIENT_TICK.register { client ->
 *     if (keyBinding.wasPressed()) {
 *         playerAttack(client.player)
 *     }
 * }
 * ```
 */
