# 🎯 Referência Rápida do Sistema de Ki

## Inicialização

### Em fabric.mod.json (se ainda não tiver):
```json
{
  "entrypoints": {
    "main": [
      "com.bernardo.dragoncraft.DragonCraftZ"
    ],
    "client": [
      "com.bernardo.dragoncraft.DragonCraftZClient"
    ]
  }
}
```

---

## Acesso Rápido

### Obter dados de um jogador
```kotlin
val data = KiManager.getPlayerKiData(player)
```

### Operações Básicas
```kotlin
// Usar Ki
KiSystem.useKi(data, 50f)

// Restaurar Ki  
KiSystem.restoreKi(data, 30f)

// Verificar se tem Ki
if (data.hasEnoughKi(50f)) { ... }
```

### Ações de Combate
```kotlin
// Ataque
KiSystem.attemptAttack(data, power = 1.0f)

// Dash
KiSystem.attemptDash(data)

// Voo
KiSystem.activateFlying(data)
```

### Carregamento
```kotlin
// Iniciar
KiSystem.startCharging(data)

// Atualizar (a cada tick)
KiSystem.updateChargeProgress(data)

// Finalizar e obter valor
val chargedKi = KiSystem.stopCharging(data)
```

### Transformações
```kotlin
// Ativar forma
KiSystem.activateTransformation(data, TransformationState.SUPER_FORM)

// Desativar
KiSystem.deactivateTransformation(data)

// Verificar se pode
if (TransformationManager.canActivate(data, form)) { ... }
```

### Aura
```kotlin
// Ativar/desativar
KiSystem.activateAura(data)
KiSystem.deactivateAura(data)

// Aumentar/diminuir intensidade
AuraSystem.increaseIntensity(data)
AuraSystem.decreaseIntensity(data)
```

### Modificadores
```kotlin
// Buff de poder
KiModifiers.applyPowerBuff(data, 1.3f, 200)  // 30% boost por 10s

// Debuff
KiModifiers.applyDrainDebuff(data, 1.5f, 100)

// Aumentar controle
KiModifiers.increaseKiControl(data, 0.1f)

// Defesa
KiModifiers.increaseKiDefense(data, 1.0f)
```

### Informações
```kotlin
// Porcentagem (0-1)
data.getKiPercent()
data.getStaminaPercent()

// Multiplicadores
KiSystem.getTotalPowerMultiplier(data)
KiSystem.getTotalSpeedMultiplier(data)

// Debug
KiSystem.getDebugInfo(data)
```

---

## Multiplicadores de Transformação

```
POWER_UP:
  - Poder: x1.5
  - Velocidade: x1.2
  - Drain: x1.3

SUPER_FORM:
  - Poder: x2.5
  - Velocidade: x1.5
  - Drain: x2.0

ULTIMATE_POWER:
  - Poder: x4.0
  - Velocidade: x2.0
  - Drain: x3.5
```

---

## Estados de Ki

```
IDLE              → Regen: x1.5
MOVING            → Regen: x0.8
CHARGING          → Regen: x0.0
ATTACKING         → Regen: x0.2
FLYING            → Regen: x0.3
DASHING           → Regen: x0.0
TRANSFORMED       → Regen: x0.5
LOW_KI (<20%)     → Regen: x1.2
```

---

## Custos Típicos

```
Voo (inicial):          5 Ki
Voo (por tick):         0.8 Ki
Dash:                   5 Ki
Ataque (básico):        8 Ki
Ataque (poder x2):      16 Ki
Charge (por tick):      0.3 Ki
Transformação base:     25 Ki
Super Form:             50 Ki
Ultimate Power:         100 Ki
Aura (por tick):        0.4 Ki
```

---

## Dicas de Performance

✓ Use `KiManager.shouldSync()` antes de enviar packets
✓ Cache dados com `getPlayerKiData()` - resultado é cacheado
✓ Não recalcule multiplicadores a cada frame - calcule quando muda
✓ Use enums (TransformationState) não strings
✓ Avoid raças em comparações hot-loop - guarde o valor

---

## Debugging

```kotlin
// Imprimir info completa
logger.info(KiSystem.getDebugInfo(data))
logger.info(KiManager.getDebugInfo())

// Verificar se dados foram salvos
val nbt = player.persistentData
val kiNbt = nbt.getCompound("KiData")
```

---

## Integração com Eventos

### Quando jogador ataca (ex: modificar dano):
```kotlin
// Em seu evento de ataque
val data = KiManager.getPlayerKiData(attacker)
val powerMult = KiSystem.getTotalPowerMultiplier(data)
damage *= powerMult
```

### Quando jogador leva dano:
```kotlin
// Já integrado em KiEventHandler!
// Aplica penalidade automaticamente
```

### Quando jogador respawna:
```kotlin
// Dados são carregados automaticamente
// (salvo antes da morte)
```

---

## Próximas Implementações

- [ ] Keybinds para transformação
- [ ] Partículas customizadas
- [ ] Sounds de transformação
- [ ] Sensor de inimigos próximos
- [ ] Combo system
- [ ] Skill tree de abilities
- [ ] Leveling system
- [ ] Achievements/objetivos

---

**Sistema desenvolvido para máxima performance e facilidade de expansão!**
