package com.bernardo.dragoncraft.mixin;

import com.bernardo.dragoncraft.client.aura.AuraRenderer;
import com.bernardo.dragoncraft.core.KiManager;
import com.bernardo.dragoncraft.core.data.PlayerKiData;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerRendererMixin {

    @Inject(
        method = "render",
        at = @At("TAIL")
    )
    private void dragoncraft$renderAura(AbstractClientPlayerEntity player,
                                        float yaw,
                                        float tickDelta,
                                        MatrixStack matrices,
                                        VertexConsumerProvider provider,
                                        int light,
                                        CallbackInfo ci) {

        PlayerKiData data = KiManager.INSTANCE.getPlayerKiDataOrNull(player.getUuid());
        if (data == null || !data.getAuraActive()) return;

        AuraRenderer.render(matrices, provider, player, tickDelta, light);
    }
}
