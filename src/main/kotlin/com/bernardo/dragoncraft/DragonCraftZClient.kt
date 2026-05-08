package com.bernardo.dragoncraft

import net.fabricmc.api.ClientModInitializer

object DragonCraftZClient : ClientModInitializer {

    override fun onInitializeClient() {
        DragonCraftZ.LOGGER.info("DragonCraft Z client initialized!")
    }
}
