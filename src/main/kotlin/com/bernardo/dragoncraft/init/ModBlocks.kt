package com.bernardo.dragoncraft.init

import com.bernardo.dragoncraft.DragonCraftZ
import com.bernardo.dragoncraft.block.*
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModBlocks {

    val DIRTYSTONE_COBBLESTONE = DirtystoneBlock(AbstractBlock.Settings.copy(Blocks.COBBLESTONE))
    val DIRTYSTONE_DIRTY = DirtystoneBlock(AbstractBlock.Settings.copy(Blocks.DIRT))
    val DIRTYSTONE_STONE = DirtystoneBlock(AbstractBlock.Settings.copy(Blocks.STONE))
    val NAMEK_GRASS = NamekGrassBlock(AbstractBlock.Settings.copy(Blocks.GRASS_BLOCK))
    val NAMEK_DIRTY = DirtystoneBlock(AbstractBlock.Settings.copy(Blocks.DIRT))
    val NAMEK_LOG = NamekLogBlock(AbstractBlock.Settings.copy(Blocks.OAK_LOG))
    val NAMEK_LEAVES = NamekLeavesBlock(AbstractBlock.Settings.copy(Blocks.OAK_LEAVES))
    val NAMEK_SAPLING = NamekSaplingBlock(AbstractBlock.Settings.copy(Blocks.OAK_SAPLING))
    val LOOKOUT_FLOOR_RED = LookoutBlock(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS))
    val LOOKOUT_FLOOR_WHITE = LookoutBlock(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS))
    val LOOKOUT_WALL = LookoutBlock(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS))
    val LOOKOUT_YELLOW_BLOCK = LookoutBlock(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS))

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
