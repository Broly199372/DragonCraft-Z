package com.bernardo.dragoncraft.item

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World

open class DragonCraftFoodItem(settings: Settings) : Item(settings) {

    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        val result = super.finishUsing(stack, world, user)
        onFoodEaten(stack, world, user)
        return result
    }

    open fun onFoodEaten(stack: ItemStack, world: World, user: LivingEntity) {}
}
