package com.bernardo.dragoncraft

import com.bernardo.dragoncraft.init.ModBlocks
import com.bernardo.dragoncraft.init.ModItems
import com.bernardo.dragoncraft.init.ModEntities
import com.bernardo.dragoncraft.init.ModSounds
import com.bernardo.dragoncraft.network.ModNetwork
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
        ModNetwork.register()

        LOGGER.info("DragonCraft Z is ready. Power level: OVER 9000!")
    }
}
