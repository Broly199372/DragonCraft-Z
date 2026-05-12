#!/bin/bash

BLOCKSTATES="src/main/resources/assets/dragoncraft/blockstates"
MODELS_BLOCK="src/main/resources/assets/dragoncraft/models/block"

mkdir -p "$BLOCKSTATES"
mkdir -p "$MODELS_BLOCK"

echo "🌳 Gerando Namek Log customizado (4x16x4)..."

# Namek Log - Blockstate com rotação
cat > "$BLOCKSTATES/namek_log.json" << 'INNER'
{
  "variants": {
    "axis=x": {
      "model": "dragoncraft:block/namek_log_horizontal",
      "x": 90,
      "y": 90
    },
    "axis=y": {
      "model": "dragoncraft:block/namek_log"
    },
    "axis=z": {
      "model": "dragoncraft:block/namek_log_horizontal",
      "x": 90
    }
  }
}
INNER

# Modelo VERTICAL (eixo Y) - 4x16x4
cat > "$MODELS_BLOCK/namek_log.json" << 'INNER'
{
  "parent": "minecraft:block/block",
  "textures": {
    "particle": "dragoncraft:block/namek_log_side",
    "end": "dragoncraft:block/namek_log_top",
    "side": "dragoncraft:block/namek_log_side"
  },
  "elements": [
    {
      "from": [6, 0, 6],
      "to": [10, 16, 10],
      "faces": {
        "down":  {"uv": [6, 6, 10, 10], "texture": "#end", "cullface": "down"},
        "up":    {"uv": [6, 6, 10, 10], "texture": "#end", "cullface": "up"},
        "north": {"uv": [6, 0, 10, 16], "texture": "#side"},
        "south": {"uv": [6, 0, 10, 16], "texture": "#side"},
        "west":  {"uv": [6, 0, 10, 16], "texture": "#side"},
        "east":  {"uv": [6, 0, 10, 16], "texture": "#side"}
      }
    }
  ]
}
INNER

# Modelo HORIZONTAL (eixo X/Z) - 16x4x4 (rotacionado)
cat > "$MODELS_BLOCK/namek_log_horizontal.json" << 'INNER'
{
  "parent": "minecraft:block/block",
  "textures": {
    "particle": "dragoncraft:block/namek_log_side",
    "end": "dragoncraft:block/namek_log_top",
    "side": "dragoncraft:block/namek_log_side"
  },
  "elements": [
    {
      "from": [0, 6, 6],
      "to": [16, 10, 10],
      "faces": {
        "down":  {"uv": [0, 6, 16, 10], "texture": "#side"},
        "up":    {"uv": [0, 6, 16, 10], "texture": "#side"},
        "north": {"uv": [0, 6, 16, 10], "texture": "#side"},
        "south": {"uv": [0, 6, 16, 10], "texture": "#side"},
        "west":  {"uv": [6, 6, 10, 10], "texture": "#end", "cullface": "west"},
        "east":  {"uv": [6, 6, 10, 10], "texture": "#end", "cullface": "east"}
      }
    }
  ]
}
INNER

# Modelo do item (vertical)
cat > "src/main/resources/assets/dragoncraft/models/item/namek_log.json" << 'INNER'
{
  "parent": "dragoncraft:block/namek_log"
}
INNER

echo "✅ Namek Log customizado (4x16x4) criado!"
echo "   - Vertical (Y): 4x16x4"
echo "   - Horizontal (X/Z): 16x4x4"

