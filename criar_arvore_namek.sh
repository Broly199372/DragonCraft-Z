#!/bin/bash

KOTLIN_DIR="src/main/kotlin/com/bernardo/dragoncraft"
mkdir -p "$KOTLIN_DIR/world/gen"

echo "🌳 Criando sistema de geração da Árvore de Namek..."

# ========================================
# 1. CONFIGURADOR DA ÁRVORE
# ========================================
cat > "$KOTLIN_DIR/world/gen/NamekTreeFeature.kt" << 'INNER'
package com.bernardo.dragoncraft.world.gen

import com.bernardo.dragoncraft.init.ModBlocks
import com.mojang.serialization.Codec
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.TestableWorld
import net.minecraft.world.gen.feature.TreeFeature
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.foliage.FoliagePlacer
import net.minecraft.world.gen.trunk.TrunkPlacer
import net.minecraft.world.gen.trunk.TrunkPlacerType
import net.minecraft.util.math.intprovider.IntProvider
import net.minecraft.world.gen.foliage.FoliagePlacerType
import com.mojang.serialization.codecs.RecordCodecBuilder

// ========================================
// TRUNK PLACER (Tronco 4-8 blocos)
// ========================================
class NamekTrunkPlacer(
    baseHeight: Int,
    firstRandomHeight: Int,
    secondRandomHeight: Int
) : TrunkPlacer(baseHeight, firstRandomHeight, secondRandomHeight) {

    override fun getType(): TrunkPlacerType<*> {
        return TYPE
    }

    override fun generate(
        world: TestableWorld,
        replacer: (BlockPos, BlockState) -> Unit,
        random: Random,
        height: Int,
        startPos: BlockPos,
        config: TreeFeatureConfig
    ): List<FoliagePlacer.TreeNode> {
        
        // Altura aleatória entre 4 e 8 blocos
        val trunkHeight = 4 + random.nextInt(5) // 4 a 8
        
        // Coloca os blocos de tronco verticalmente
        for (i in 0 until trunkHeight) {
            val pos = startPos.up(i)
            replacer(pos, ModBlocks.NAMEK_LOG.defaultState)
        }
        
        // Retorna a posição do topo para colocar as folhas
        return listOf(FoliagePlacer.TreeNode(startPos.up(trunkHeight), 0, false))
    }

    companion object {
        val CODEC: Codec<NamekTrunkPlacer> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.INT.fieldOf("base_height").forGetter { it.baseHeight },
                Codec.INT.fieldOf("height_rand_a").forGetter { it.firstRandomHeight },
                Codec.INT.fieldOf("height_rand_b").forGetter { it.secondRandomHeight }
            ).apply(instance, ::NamekTrunkPlacer)
        }
        
        lateinit var TYPE: TrunkPlacerType<NamekTrunkPlacer>
    }
}

// ========================================
// FOLIAGE PLACER (Copa 3x3x3)
// ========================================
class NamekFoliagePlacer(
    radius: IntProvider,
    offset: IntProvider
) : FoliagePlacer(radius, offset) {

    override fun getType(): FoliagePlacerType<*> {
        return TYPE
    }

    override fun generate(
        world: TestableWorld,
        placer: (BlockPos, BlockState) -> Unit,
        random: Random,
        config: TreeFeatureConfig,
        trunkHeight: Int,
        treeNode: TreeNode,
        foliageHeight: Int,
        radius: Int,
        offset: Int
    ) {
        val centerPos = treeNode.center
        
        // Gera um cubo 3x3x3 de folhas
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    val leafPos = centerPos.add(x, y, z)
                    placer(leafPos, ModBlocks.NAMEK_LEAVES.defaultState)
                }
            }
        }
    }

    override fun getRandomHeight(random: Random, trunkHeight: Int, config: TreeFeatureConfig): Int {
        return 3 // Altura da copa
    }

    override fun isInvalidForLeaves(
        random: Random,
        dx: Int,
        y: Int,
        dz: Int,
        radius: Int,
        giantTrunk: Boolean
    ): Boolean {
        return false
    }

    companion object {
        val CODEC: Codec<NamekFoliagePlacer> = RecordCodecBuilder.create { instance ->
            instance.group(
                IntProvider.createValidatingCodec(0, 16).fieldOf("radius").forGetter { it.radius },
                IntProvider.createValidatingCodec(0, 16).fieldOf("offset").forGetter { it.offset }
            ).apply(instance, ::NamekFoliagePlacer)
        }
        
        lateinit var TYPE: FoliagePlacerType<NamekFoliagePlacer>
    }
}
INNER

# ========================================
# 2. GERADOR DE SAPLING (NamekSaplingGenerator)
# ========================================
cat > "$KOTLIN_DIR/world/gen/NamekSaplingGenerator.kt" << 'INNER'
package com.bernardo.dragoncraft.world.gen

import com.bernardo.dragoncraft.init.ModBlocks
import net.minecraft.block.sapling.SaplingGenerator
import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.random.Random
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.util.math.intprovider.ConstantIntProvider

class NamekSaplingGenerator : SaplingGenerator() {

    override fun getTreeFeature(random: Random, bees: Boolean): RegistryKey<ConfiguredFeature<*, *>>? {
        return null // Vamos usar createTreeFeature ao invés
    }
    
    override fun createTreeFeature(random: Random, bees: Boolean): TreeFeatureConfig {
        return TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.NAMEK_LOG),
            NamekTrunkPlacer(4, 2, 2), // Base 4, rand +0-4 = 4-8 blocos
            BlockStateProvider.of(ModBlocks.NAMEK_LEAVES),
            NamekFoliagePlacer(ConstantIntProvider.create(1), ConstantIntProvider.create(0)),
            TwoLayersFeatureSize(1, 0, 1)
        ).build()
    }
}
INNER

# ========================================
# 3. ATUALIZAR NamekSaplingBlock.kt
# ========================================
cat > "$KOTLIN_DIR/block/NamekSaplingBlock.kt" << 'INNER'
package com.bernardo.dragoncraft.block

import com.bernardo.dragoncraft.world.gen.NamekSaplingGenerator
import net.minecraft.block.SaplingBlock

class NamekSaplingBlock(settings: Settings) : SaplingBlock(NamekSaplingGenerator(), settings)
INNER

# ========================================
# 4. REGISTRAR OS TIPOS CUSTOMIZADOS
# ========================================
cat > "$KOTLIN_DIR/init/ModWorldGen.kt" << 'INNER'
package com.bernardo.dragoncraft.init

import com.bernardo.dragoncraft.DragonCraftZ
import com.bernardo.dragoncraft.world.gen.NamekTrunkPlacer
import com.bernardo.dragoncraft.world.gen.NamekFoliagePlacer
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.world.gen.trunk.TrunkPlacerType
import net.minecraft.world.gen.foliage.FoliagePlacerType

object ModWorldGen {

    fun register() {
        DragonCraftZ.LOGGER.info("Registering Namek Tree Generation...")
        
        // Registrar TrunkPlacer customizado
        NamekTrunkPlacer.TYPE = Registry.register(
            Registries.TRUNK_PLACER_TYPE,
            Identifier(DragonCraftZ.MOD_ID, "namek_trunk_placer"),
            TrunkPlacerType { NamekTrunkPlacer.CODEC }
        )
        
        // Registrar FoliagePlacer customizado
        NamekFoliagePlacer.TYPE = Registry.register(
            Registries.FOLIAGE_PLACER_TYPE,
            Identifier(DragonCraftZ.MOD_ID, "namek_foliage_placer"),
            FoliagePlacerType { NamekFoliagePlacer.CODEC }
        )
        
        DragonCraftZ.LOGGER.info("Namek Tree registered successfully! 🌳")
    }
}
INNER

# ========================================
# 5. ATUALIZAR DragonCraftZ.kt MAIN
# ========================================
echo ""
echo "⚠️  IMPORTANTE: Adicione esta linha no DragonCraftZ.kt:"
echo ""
cat << 'INNER'
// Em DragonCraftZ.kt, dentro do onInitialize():

override fun onInitialize() {
    LOGGER.info("DragonCraft Z awakening... 🐉")

    ModItems.register()
    ModBlocks.register()
    ModEntities.register()
    ModSounds.register()
    ModNetwork.register()
    ModWorldGen.register()  // ← ADICIONE ESTA LINHA

    LOGGER.info("DragonCraft Z is ready. Power level: OVER 9000!")
}
INNER

echo ""
echo "✅ Sistema de árvore de Namek criado!"
echo "🌳 Características:"
echo "   - Tronco: 4-8 blocos de altura (aleatório)"
echo "   - Copa: 3x3x3 blocos de folhas"
echo "   - Cresce automaticamente do sapling"

