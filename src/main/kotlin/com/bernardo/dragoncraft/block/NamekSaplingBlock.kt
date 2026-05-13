package com.bernardo.dragoncraft.block

import net.minecraft.block.SaplingBlock
import net.minecraft.block.sapling.SaplingGenerator
import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.random.Random
import net.minecraft.world.gen.feature.ConfiguredFeature

class NamekSaplingBlock(settings: Settings) : SaplingBlock(NamekTreeGenerator(), settings)

// Por enquanto usa Oak, mas com nome customizado
class NamekTreeGenerator : SaplingGenerator() {
    override fun getTreeFeature(random: Random, bees: Boolean): RegistryKey<ConfiguredFeature<*, *>>? {
        // Retorna null = usa comportamento padrão do Minecraft
        return null
    }
}
