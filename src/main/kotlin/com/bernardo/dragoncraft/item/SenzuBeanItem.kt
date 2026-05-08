package com.bernardo.dragoncraft.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.FoodComponent
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

class SenzuBeanItem : DragonCraftFoodItem(
    Settings()
        .food(
            FoodComponent.Builder()
                .hunger(20)
                .saturationModifier(2.0f)
                .alwaysEdible()
                .build()
        )
        .maxCount(16)
) {

    override fun getUseAction(stack: ItemStack): UseAction {
        return UseAction.EAT
    }

    override fun getMaxUseTime(stack: ItemStack): Int {
        return 32
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        return TypedActionResult.consume(user.getStackInHand(hand))
    }

    override fun onFoodEaten(stack: ItemStack, world: World, user: LivingEntity) {
        if (!world.isClient && user is PlayerEntity) {
            user.health = user.maxHealth
            user.clearStatusEffects()
        }
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        tooltip.add(Text.literal("§aRestores full health and hunger"))
        tooltip.add(Text.literal("§7Clears all status effects"))
    }
}
