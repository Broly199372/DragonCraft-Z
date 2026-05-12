#!/bin/bash

OUTPUT_FILE="codigo_completo.txt"

# Limpa o arquivo de saída
> "$OUTPUT_FILE"

echo "==================================" >> "$OUTPUT_FILE"
echo "CÓDIGO DO MOD DRAGONCRAFT" >> "$OUTPUT_FILE"
echo "Data: $(date)" >> "$OUTPUT_FILE"
echo "==================================" >> "$OUTPUT_FILE"
echo "" >> "$OUTPUT_FILE"

# Arquivos binários/recursos apenas listar
echo "========================================" >> "$OUTPUT_FILE"
echo "ESTRUTURA DE ARQUIVOS BINÁRIOS/RECURSOS" >> "$OUTPUT_FILE"
echo "========================================" >> "$OUTPUT_FILE"
find src -type f \( -name "*.png" -o -name "*.jpg" -o -name "*.ogg" -o -name "*.mcmeta" -o -name "*.obj" -o -name "*.mtl" -o -name "*.gltf" \) | sort >> "$OUTPUT_FILE"
echo "" >> "$OUTPUT_FILE"
echo "" >> "$OUTPUT_FILE"

# Arquivos de código - mostrar conteúdo completo
find src -type f \( -name "*.java" -o -name "*.kt" -o -name "*.json" -o -name "*.gradle" -o -name "*.kts" -o -name "*.properties" -o -name "*.toml" \) | sort | while read -r file; do
    echo "========================================" >> "$OUTPUT_FILE"
    echo "ARQUIVO: $file" >> "$OUTPUT_FILE"
    echo "========================================" >> "$OUTPUT_FILE"
    cat "$file" >> "$OUTPUT_FILE"
    echo "" >> "$OUTPUT_FILE"
    echo "" >> "$OUTPUT_FILE"
done

echo "✅ Código extraído para: $OUTPUT_FILE"
wc -l "$OUTPUT_FILE"

