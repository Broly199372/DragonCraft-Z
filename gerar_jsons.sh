#!/bin/bash

# Diretórios
BLOCKSTATES="src/main/resources/assets/dragoncraft/blockstates"
MODELS_BLOCK="src/main/resources/assets/dragoncraft/models/block"
MODELS_ITEM="src/main/resources/assets/dragoncraft/models/item"

# Criar diretórios se não existirem
mkdir -p "$BLOCKSTATES"
mkdir -p "$MODELS_BLOCK"

echo "🔧 Gerando JSONs do DragonCraft..."

# ========================================
# DIRTYSTONE BLOCKS
# ========================================

# Dirtystone Cobblestone
cat > "$BLOCKSTATES/dirtystone_cobblestone.json" << 'INNER'
{
  "variants": {
    "": {
      "model": "dragoncraft:block/dirtystone_cobblestone"
    }
  }
}
INNER

cat > "$MODELS_BLOCK/dirtystone_cobblestone.json" << 'INNER'
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "dragoncraft:block/dirtystone_cobblestone"
  }
}
INNER

# Dirtystone Dirty
cat > "$BLOCKSTATES/dirtystone_dirty.json" << 'INNER'
{
  "variants": {
    "": {
      "model": "dragoncraft:block/dirtystone_dirty"
    }
  }
}
INNER

cat > "$MODELS_BLOCK/dirtystone_dirty.json" << 'INNER'
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "dragoncraft:block/dirtystone_dirty"
  }
}
INNER

# Dirtystone Stone
cat > "$BLOCKSTATES/dirtystone_stone.json" << 'INNER'
{
  "variants": {
    "": {
      "model": "dragoncraft:block/dirtystone_stone"
    }
  }
}
INNER

cat > "$MODELS_BLOCK/dirtystone_stone.json" << 'INNER'
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "dragoncraft:block/dirtystone_stone"
  }
}
INNER

# ========================================
# NAMEK BLOCKS
# ========================================

# Namek Grass (COM 3 TEXTURAS: top, side, bottom)
cat > "$BLOCKSTATES/namek_grass.json" << 'INNER'
{
  "variants": {
    "": {
      "model": "dragoncraft:block/namek_grass"
    }
  }
}
INNER

cat > "$MODELS_BLOCK/namek_grass.json" << 'INNER'
{
  "parent": "minecraft:block/block",
  "textures": {
    "particle": "dragoncraft:block/namek_grass_top",
    "bottom": "dragoncraft:block/namek_dirty",
    "top": "dragoncraft:block/namek_grass_top",
    "side": "dragoncraft:block/namek_grass_side"
  },
  "elements": [
    {
      "from": [0, 0, 0],
      "to": [16, 16, 16],
      "faces": {
        "down":  {"uv": [0, 0, 16, 16], "texture": "#bottom", "cullface": "down"},
        "up":    {"uv": [0, 0, 16, 16], "texture": "#top",    "cullface": "up"},
        "north": {"uv": [0, 0, 16, 16], "texture": "#side",   "cullface": "north"},
        "south": {"uv": [0, 0, 16, 16], "texture": "#side",   "cullface": "south"},
        "west":  {"uv": [0, 0, 16, 16], "texture": "#side",   "cullface": "west"},
        "east":  {"uv": [0, 0, 16, 16], "texture": "#side",   "cullface": "east"}
      }
    }
  ]
}
INNER

# Namek Dirty
cat > "$BLOCKSTATES/namek_dirty.json" << 'INNER'
{
  "variants": {
    "": {
      "model": "dragoncraft:block/namek_dirty"
    }
  }
}
INNER

cat > "$MODELS_BLOCK/namek_dirty.json" << 'INNER'
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "dragoncraft:block/namek_dirty"
  }
}
INNER

# Namek Log (COM ROTAÇÃO - axis)
cat > "$BLOCKSTATES/namek_log.json" << 'INNER'
{
  "variants": {
    "axis=x": {
      "model": "dragoncraft:block/namek_log",
      "x": 90,
      "y": 90
    },
    "axis=y": {
      "model": "dragoncraft:block/namek_log"
    },
    "axis=z": {
      "model": "dragoncraft:block/namek_log",
      "x": 90
    }
  }
}
INNER

cat > "$MODELS_BLOCK/namek_log.json" << 'INNER'
{
  "parent": "minecraft:block/cube_column",
  "textures": {
    "end": "dragoncraft:block/namek_log_top",
    "side": "dragoncraft:block/namek_log_side"
  }
}
INNER

# Namek Leaves
cat > "$BLOCKSTATES/namek_leaves.json" << 'INNER'
{
  "variants": {
    "": {
      "model": "dragoncraft:block/namek_leaves"
    }
  }
}
INNER

cat > "$MODELS_BLOCK/namek_leaves.json" << 'INNER'
{
  "parent": "minecraft:block/leaves",
  "textures": {
    "all": "dragoncraft:block/namek_leave"
  }
}
INNER

# Namek Sapling
cat > "$BLOCKSTATES/namek_sapling.json" << 'INNER'
{
  "variants": {
    "": {
      "model": "dragoncraft:block/namek_sapling"
    }
  }
}
INNER

cat > "$MODELS_BLOCK/namek_sapling.json" << 'INNER'
{
  "parent": "minecraft:block/cross",
  "textures": {
    "cross": "dragoncraft:block/namek_sapling"
  }
}
INNER

# ========================================
# LOOKOUT BLOCKS (Torre do Kami-sama)
# ========================================

# Lookout Floor Red
cat > "$BLOCKSTATES/lookout_floor_red.json" << 'INNER'
{
  "variants": {
    "": {
      "model": "dragoncraft:block/lookout_floor_red"
    }
  }
}
INNER

cat > "$MODELS_BLOCK/lookout_floor_red.json" << 'INNER'
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "dragoncraft:block/lookout_floor_red"
  }
}
INNER

# Lookout Floor White
cat > "$BLOCKSTATES/lookout_floor_white.json" << 'INNER'
{
  "variants": {
    "": {
      "model": "dragoncraft:block/lookout_floor_white"
    }
  }
}
INNER

cat > "$MODELS_BLOCK/lookout_floor_white.json" << 'INNER'
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "dragoncraft:block/lookout_floor_white"
  }
}
INNER

# Lookout Wall
cat > "$BLOCKSTATES/lookout_wall.json" << 'INNER'
{
  "variants": {
    "": {
      "model": "dragoncraft:block/lookout_wall"
    }
  }
}
INNER

cat > "$MODELS_BLOCK/lookout_wall.json" << 'INNER'
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "dragoncraft:block/lookout_wall"
  }
}
INNER

# Lookout Yellow Block
cat > "$BLOCKSTATES/lookout_yellow_block.json" << 'INNER'
{
  "variants": {
    "": {
      "model": "dragoncraft:block/lookout_yellow_block"
    }
  }
}
INNER

cat > "$MODELS_BLOCK/lookout_yellow_block.json" << 'INNER'
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "dragoncraft:block/lookout_yellow_block"
  }
}
INNER

echo "✅ Blockstates criados: $BLOCKSTATES"
echo "✅ Modelos de bloco criados: $MODELS_BLOCK"
echo "🐉 DragonCraft JSONs gerados com sucesso!"

