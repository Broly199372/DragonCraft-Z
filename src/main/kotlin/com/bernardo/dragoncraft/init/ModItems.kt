package com.bernardo.dragoncraft.init

import com.bernardo.dragoncraft.DragonCraftZ
import com.bernardo.dragoncraft.item.DinoMeatRawItem
import com.bernardo.dragoncraft.item.DinoMeatCookedItem
import com.bernardo.dragoncraft.item.SenzuBeanItem
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object ModItems {

    val DINO_MEAT_RAW = DinoMeatRawItem()
    val DINO_MEAT_COOKED = DinoMeatCookedItem()
    val SENZU_BEAN = SenzuBeanItem()

    val DRAGONCRAFT_GROUP: RegistryKey<ItemGroup> = RegistryKey.of(
        Registries.ITEM_GROUP.key,
        Identifier(DragonCraftZ.MOD_ID, "dragoncraft_group")
    )

    fun register() {
        registerItem("dino_meat_raw", DINO_MEAT_RAW)
        registerItem("dino_meat_cooked_raw", DINO_MEAT_COOKED)
        registerItem("senzu_bean", SENZU_BEAN)

        Registry.register(
            Registries.ITEM_GROUP,
            DRAGONCRAFT_GROUP,
            FabricItemGroup.builder()
                .displayName(Text.literal("DragonCraft Z"))
                .icon { ItemStack(SENZU_BEAN) }
                .build()
        )

        ItemGroupEvents.modifyEntriesEvent(DRAGONCRAFT_GROUP).register { group ->
            group.add(DINO_MEAT_RAW)
            group.add(DINO_MEAT_COOKED)
            group.add(SENZU_BEAN)
        }
    }

    private fun registerItem(name: String, item: Item) {
        Registry.register(
            Registries.ITEM,
            Identifier(DragonCraftZ.MOD_ID, name),
            item
        )
    }
}
