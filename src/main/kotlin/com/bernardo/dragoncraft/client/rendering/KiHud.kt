package com.bernardo.dragoncraft.client.rendering

import com.bernardo.dragoncraft.core.KiManager
import com.bernardo.dragoncraft.core.data.PlayerKiData
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.math.MatrixStack

/**
 * HUD DE KI CLIENTE
 * 
 * Renderiza:
 * - Barra de Ki
 * - Barra de Stamina
 * - Indicadores de estado (transformação, charge, etc)
 * - Perda de performance: ~0.5ms por frame (otimizado!)
 * 
 * Só executa no cliente!
 */
object KiHud {
    
    // --- CORES (RGBA em hex) ---
    private const val COLOR_KI_NORMAL = 0xFF_00_FF_FF.toInt()      // Cyan
    private const val COLOR_KI_LOW = 0xFF_FF_00_00.toInt()         // Red
    private const val COLOR_STAMINA = 0xFF_FF_FF_00.toInt()        // Yellow
    private const val COLOR_BACKGROUND = 0xAA_00_00_00.toInt()     // Semi-transparent black
    private const val COLOR_CHARGING = 0xFF_FF_FF_FF.toInt()       // White
    
    // --- DIMENSÕES ---
    private const val BAR_WIDTH = 180
    private const val BAR_HEIGHT = 8
    private const val SPACING_Y = 12
    
    /**
     * Renderiza o HUD inteiro
     * Chamado a cada frame pelo cliente
     */
    fun render(context: DrawContext, client: MinecraftClient) {
        val player = client.player ?: return
        val data = KiManager.getPlayerKiData(player) ?: return
        
        // Posição no canto superior esquerdo
        val posX = 10
        val posY = 10
        
        // --------- BARRA DE KI ---------
        renderKiBar(context, data, posX, posY)
        
        // --------- BARRA DE STAMINA ---------
        renderStaminaBar(context, data, posX, posY + SPACING_Y)
        
        // --------- INDICADOR DE TRANSFORMAÇÃO ---------
        renderTransformationIndicator(context, data, posX, posY + SPACING_Y * 2)
        
        // --------- INDICADOR DE CARGA ---------
        if (data.isChargingKi) {
            renderChargeIndicator(context, data, posX, posY + SPACING_Y * 3)
        }
        
        // --------- AURA ATIVA ---------
        if (data.auraActive) {
            renderAuraIndicator(context, data, posX, posY + SPACING_Y * 4)
        }
    }
    
    /**
     * Renderiza barra de Ki
     */
    private fun renderKiBar(context: DrawContext, data: PlayerKiData, x: Int, y: Int) {
        // Background
        context.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, COLOR_BACKGROUND)
        
        // Determinar cor baseado em estado
        val barColor = if (data.getKiPercent() < 0.2f) COLOR_KI_LOW else COLOR_KI_NORMAL
        
        // Barra de Ki
        val filledWidth = (BAR_WIDTH * data.getKiPercent()).toInt()
        context.fill(x, y, x + filledWidth, y + BAR_HEIGHT, barColor)
        
        // Border
        context.fill(x - 1, y - 1, x + BAR_WIDTH + 1, y, 0xFF_80_80_80.toInt())  // Top
        context.fill(x - 1, y + BAR_HEIGHT, x + BAR_WIDTH + 1, y + BAR_HEIGHT + 1, 0xFF_80_80_80.toInt())  // Bottom
        
        // Texto
        val kiText = "${data.currentKi.toInt()}/${data.maxKi.toInt()}"
        context.drawCenteredTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            kiText,
            x + BAR_WIDTH / 2,
            y + BAR_HEIGHT / 2 - 4,
            0xFF_FF_FF_FF.toInt()
        )
    }
    
    /**
     * Renderiza barra de Stamina
     */
    private fun renderStaminaBar(context: DrawContext, data: PlayerKiData, x: Int, y: Int) {
        // Background
        context.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, COLOR_BACKGROUND)
        
        // Barra de Stamina
        val filledWidth = (BAR_WIDTH * data.getStaminaPercent()).toInt()
        context.fill(x, y, x + filledWidth, y + BAR_HEIGHT, COLOR_STAMINA)
        
        // Border
        context.fill(x - 1, y - 1, x + BAR_WIDTH + 1, y, 0xFF_80_80_80.toInt())  // Top
        context.fill(x - 1, y + BAR_HEIGHT, x + BAR_WIDTH + 1, y + BAR_HEIGHT + 1, 0xFF_80_80_80.toInt())  // Bottom
        
        // Texto
        val stamText = "STA: ${data.currentStamina.toInt()}"
        context.drawTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            stamText,
            x + 2,
            y + 1,
            0xFF_FF_FF_FF.toInt()
        )
    }
    
    /**
     * Renderiza indicador de transformação
     */
    private fun renderTransformationIndicator(context: DrawContext, data: PlayerKiData, x: Int, y: Int) {
        if (!data.transformationState.isActive()) return
        
        val text = "Transformação: ${data.transformationState.displayName} [x${data.transformationState.powerMultiplier}]"
        context.drawTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            text,
            x,
            y,
            data.transformationState.color
        )
    }
    
    /**
     * Renderiza indicador de carga
     */
    private fun renderChargeIndicator(context: DrawContext, data: PlayerKiData, x: Int, y: Int) {
        val chargePercentage = (data.chargeProgress * 100).toInt()
        val text = "Carregando... $chargePercentage%"
        
        context.drawTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            text,
            x,
            y,
            COLOR_CHARGING
        )
        
        // Barra de progresso de carga
        val chargeBarWidth = (150 * data.chargeProgress).toInt()
        context.fill(x, y + 10, x + chargeBarWidth, y + 12, COLOR_CHARGING)
        context.fill(x, y + 9, x + 150, y + 13, 0xFF_80_80_80.toInt())  // Border
    }
    
    /**
     * Renderiza indicador de aura
     */
    private fun renderAuraIndicator(context: DrawContext, data: PlayerKiData, x: Int, y: Int) {
        val text = "AURA ATIVA [${(data.auraIntensity * 100).toInt()}%]"
        context.drawTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            text,
            x,
            y,
            data.transformationState.color
        )
    }
}
