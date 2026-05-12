#!/bin/bash

echo "🔧 Limpando build anterior..."
./gradlew clean

echo "🗑️ Removendo cache do Gradle..."
rm -rf .gradle
rm -rf build

echo "📦 Baixando dependências novamente..."
./gradlew --refresh-dependencies

echo "🔨 Tentando build novamente..."
./gradlew build --stacktrace

