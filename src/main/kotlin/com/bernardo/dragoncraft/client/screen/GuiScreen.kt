package com.bernardo.dragoncraft.client.screen

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.gui.DrawContext

object GuiScreen : HudRenderCallback {

    fun register() {
        HudRenderCallback.EVENT.register(this)
    }

    override fun onHudRender(drawContext: DrawContext, tickDelta: Float) {
        val screenWidth = drawContext.scaledWindowWidth
        val width = 160
        val height = 10
        val x = 5
        val y = 5
        
        drawContext.fill(x, y, x + width, y + height, 0xFF000000.toInt())
    }
}
