package com.bernardo.dragoncraft

import com.bernardo.dragoncraft.client.rendering.KiHud
import com.bernardo.dragoncraft.core.network.KiNetworking
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback

object DragonCraftZClient : ClientModInitializer {

    override fun onInitializeClient() {
        DragonCraftZ.LOGGER.info("DragonCraft Z client initialized!")
    
        KiNetworking.registerClient()

        // ===== SISTEMA DE KI - RENDERIZAÇÃO HUD =====
        try {
            HudRenderCallback.EVENT.register { context, tickCounter ->
                KiHud.render(context, net.minecraft.client.MinecraftClient.getInstance())
            }
            DragonCraftZ.LOGGER.info("✓ HUD de Ki registrado com sucesso")
        } catch (e: Exception) {
            DragonCraftZ.LOGGER.error("✗ Erro ao registrar HUD de Ki:", e)
        }
    }
}
