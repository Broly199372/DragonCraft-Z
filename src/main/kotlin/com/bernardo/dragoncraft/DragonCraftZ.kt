package com.bernardo.dragoncraft

import com.bernardo.dragoncraft.core.events.KiEventHandler
import com.bernardo.dragoncraft.core.network.KiNetworking
import com.bernardo.dragoncraft.init.ModBlocks
import com.bernardo.dragoncraft.init.ModEntities
import com.bernardo.dragoncraft.init.ModItems
import com.bernardo.dragoncraft.init.ModSounds
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object DragonCraftZ : ModInitializer {

    const val MOD_ID = "dragoncraft"
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        LOGGER.info("DragonCraft Z awakening... 🐉")

        ModItems.register()
        ModBlocks.register()
        ModEntities.register()
        ModSounds.register()
        KiNetworking.registerServer()

        try {
            KiEventHandler.registerEvents()
            LOGGER.info("✓ Sistema de Ki inicializado com sucesso")
        } catch (e: Exception) {
            LOGGER.error("✗ Erro ao inicializar sistema de Ki:", e)
        }

        LOGGER.info("DragonCraft Z is ready. Power level: OVER 9000!")
    }
}
