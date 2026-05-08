package com.bernardo.dragoncraft.init

import com.bernardo.dragoncraft.DragonCraftZ
import com.bernardo.dragoncraft.block.*
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModBlocks {

    val DIRTYSTONE_COBBLESTONE = DirtystoneBlock(FabricBlockSettings.copyOf(Blocks.COBBLESTONE))
    val DIRTYSTONE_DIRTY = DirtystoneBlock(FabricBlockSettings.copyOf(Blocks.DIRT))
    val DIRTYSTONE_STONE = DirtystoneBlock(FabricBlockSettings.copyOf(Blocks.STONE))

    val NAMEK_GRASS = NamekGrassBlock(FabricBlockSettings.copyOf(Blocks.GRASS_BLOCK))
    val NAMEK_DIRTY = DirtystoneBlock(FabricBlockSettings.copyOf(Blocks.DIRT))
    val NAMEK_LOG = NamekLogBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG))
    val NAMEK_LEAVES = NamekLeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES))
    val NAMEK_SAPLING = NamekSaplingBlock(FabricBlockSettings.copyOf(Blocks.OAK_SAPLING))

    val LOOKOUT_FLOOR_RED = LookoutBlock(FabricBlockSettings.copyOf(Blocks.STONE_BRICKS))
    val LOOKOUT_FLOOR_WHITE = LookoutBlock(FabricBlockSettings.copyOf(Blocks.STONE_BRICKS))
    val LOOKOUT_WALL = LookoutBlock(FabricBlockSettings.copyOf(Blocks.STONE_BRICKS))
    val LOOKOUT_YELLOW_BLOCK = LookoutBlock(FabricBlockSettings.copyOf(Blocks.STONE_BRICKS))

    fun register() {
        registerBlock("dirtystone_cobblestone", DIRTYSTONE_COBBLESTONE)
        registerBlock("dirtystone_dirty", DIRTYSTONE_DIRTY)
        registerBlock("dirtystone_stone", DIRTYSTONE_STONE)
        registerBlock("namek_grass", NAMEK_GRASS)
        registerBlock("namek_dirty", NAMEK_DIRTY)
        registerBlock("namek_log", NAMEK_LOG)
        registerBlock("namek_leaves", NAMEK_LEAVES)
        registerBlock("namek_sapling", NAMEK_SAPLING)
        registerBlock("lookout_floor_red", LOOKOUT_FLOOR_RED)
        registerBlock("lookout_floor_white", LOOKOUT_FLOOR_WHITE)
        registerBlock("lookout_wall", LOOKOUT_WALL)
        registerBlock("lookout_yellow_block", LOOKOUT_YELLOW_BLOCK)

        ItemGroupEvents.modifyEntriesEvent(ModItems.DRAGONCRAFT_GROUP).register { group ->
            group.add(DIRTYSTONE_COBBLESTONE)
            group.add(DIRTYSTONE_DIRTY)
            group.add(DIRTYSTONE_STONE)
            group.add(NAMEK_GRASS)
            group.add(NAMEK_DIRTY)
            group.add(NAMEK_LOG)
            group.add(NAMEK_LEAVES)
            group.add(NAMEK_SAPLING)
            group.add(LOOKOUT_FLOOR_RED)
            group.add(LOOKOUT_FLOOR_WHITE)
            group.add(LOOKOUT_WALL)
            group.add(LOOKOUT_YELLOW_BLOCK)
        }
    }

    private fun registerBlock(name: String, block: Block) {
        Registry.register(Registries.BLOCK, Identifier(DragonCraftZ.MOD_ID, name), block)
        Registry.register(Registries.ITEM, Identifier(DragonCraftZ.MOD_ID, name), BlockItem(block, Item.Settings()))
    }
}
