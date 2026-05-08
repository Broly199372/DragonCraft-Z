package com.bernardo.dragoncraft.item

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class DinoMeatRawItem : DragonCraftFoodItem(
    Settings()
        .food(
            FoodComponent.Builder()
                .hunger(3)
                .saturationModifier(0.3f)
                .meat()
                .build()
        )
) {

    override fun onFoodEaten(stack: ItemStack, world: World, user: LivingEntity) {
        if (!world.isClient && user is PlayerEntity) {
            user.addStatusEffect(
                StatusEffectInstance(StatusEffects.HUNGER, 200, 0)
            )
            user.addStatusEffect(
                StatusEffectInstance(StatusEffects.POISON, 100, 0)
            )
        }
    }
}
