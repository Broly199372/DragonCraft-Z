package com.bernardo.dragoncraft.mixin;

import com.bernardo.dragoncraft.core.KiManager;
import com.bernardo.dragoncraft.core.KiSystem;
import com.bernardo.dragoncraft.core.data.PlayerKiData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin para salvar e carregar dados de Ki no NBT do jogador.
 * 
 * IMPORTANTE: Este arquivo DEVE ser Java (.java), NÃO Kotlin (.kt)!
 * Mixins em Kotlin são incompatíveis com SpongePowered Mixin.
 * 
 * Injeta nos métodos writeCustomDataToNbt e readCustomDataFromNbt
 * do PlayerEntity para persistir os dados de Ki entre sessões.
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerDataMixin {

    /**
     * Salva dados de Ki quando o jogador é salvo no mundo.
     * Chamado automaticamente ao sair, ao salvar o mundo, etc.
     */
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void dragoncraft$saveKiData(NbtCompound nbt, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        PlayerKiData data = KiManager.INSTANCE.getPlayerKiDataOrNull(player.getUuid());
        if (data != null) {
            NbtCompound kiNbt = new NbtCompound();
            data.writeToNbt(kiNbt);
            nbt.put("DragonCraftKi", kiNbt);
        }
    }

    /**
     * Carrega dados de Ki quando o jogador é carregado do mundo.
     * Chamado automaticamente ao entrar no mundo, ao respawnar, etc.
     */
    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void dragoncraft$loadKiData(NbtCompound nbt, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (nbt.contains("DragonCraftKi")) {
            NbtCompound kiNbt = nbt.getCompound("DragonCraftKi");
            PlayerKiData data = new PlayerKiData();
            data.readFromNbt(kiNbt);
            KiManager.INSTANCE.setPlayerKiData(player, data);
            KiSystem.INSTANCE.validateData(data);
        }
    }
}
