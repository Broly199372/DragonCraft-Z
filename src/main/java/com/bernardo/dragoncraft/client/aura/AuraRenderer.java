package com.bernardo.dragoncraft.client.aura;

import com.bernardo.dragoncraft.DragonCraftZ;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;

public class AuraRenderer {

    private static final Identifier AURA_TEXTURE =
        new Identifier(DragonCraftZ.MOD_ID, "textures/entity/aura.png");

    // 6 layers: [escala, opacidade, velocidade de rotação, cor 0=dourado 1=azul]
    private static final float[][] LAYERS = {
        { 0.55f, 0.35f,  0.8f, 0 },  // layer 1 - dourado, interno, mais denso
        { 0.70f, 0.28f, -1.0f, 1 },  // layer 2 - azul, gira ao contrário
        { 0.85f, 0.20f,  1.3f, 0 },  // layer 3 - dourado, médio
        { 1.00f, 0.15f, -0.7f, 1 },  // layer 4 - azul, médio-externo
        { 1.20f, 0.10f,  0.5f, 0 },  // layer 5 - dourado, externo
        { 1.40f, 0.08f, -0.4f, 1 },  // layer 6 - azul, mais externo, quase sumindo
    };

    // Oval: largura menor que altura pra abraçar o corpo
    private static final float OVAL_W = 0.55f;
    private static final float OVAL_H = 1.10f;

    public static void render(MatrixStack matrices,
                              VertexConsumerProvider provider,
                              PlayerEntity player,
                              float tickDelta,
                              int light) {

        float time = (player.age + tickDelta) * 0.05f;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);

        VertexConsumer buffer = provider.getBuffer(
            RenderLayer.getEntityTranslucentCull(AURA_TEXTURE)
        );

        matrices.push();
        // Centraliza no meio do corpo
        matrices.translate(0.0, 1.0, 0.0);

        for (float[] layer : LAYERS) {
            float scale    = layer[0];
            float alpha    = layer[1];
            float speed    = layer[2];
            boolean isBlue = layer[3] == 1;

            // Pulso suave e único por layer
            float pulse = 1.0f + 0.06f * MathHelper.sin(time * 3.0f + scale * 5f);

            matrices.push();

            // Rotação lenta no eixo Y
            float angle = time * speed * 20f;
            matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y
                .rotationDegrees(angle));

            float finalScale = scale * pulse;
            matrices.scale(finalScale, finalScale, finalScale);

            // Cor: dourado ou azul Ki
            float r, g, b;
            if (isBlue) {
                r = 0.25f; g = 0.55f; b = 1.00f;
            } else {
                r = 1.00f; g = 0.80f; b = 0.10f;
            }

            // Desenha 2 quads cruzados (X e Z) pra dar volume sem billboard completo
            drawOvalQuad(matrices, buffer, OVAL_W, OVAL_H, r, g, b, alpha, light, 0f);
            drawOvalQuad(matrices, buffer, OVAL_W, OVAL_H, r, g, b, alpha * 0.7f, light, 90f);

            matrices.pop();
        }

        matrices.pop();

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    private static void drawOvalQuad(MatrixStack matrices, VertexConsumer buffer,
                                     float w, float h,
                                     float r, float g, float b, float a,
                                     int light, float extraRotDeg) {

        matrices.push();
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y
            .rotationDegrees(extraRotDeg));

        Matrix4f m = matrices.peek().getPositionMatrix();

        int ri = (int)(r * 255);
        int gi = (int)(g * 255);
        int bi = (int)(b * 255);
        int ai = (int)(MathHelper.clamp(a, 0f, 1f) * 255);

        float top    =  h * 0.5f;
        float bottom = -h * 0.5f;

        // Quad oval: vértices com UV pra usar o gradiente da textura
        buffer.vertex(m, -w, bottom, 0).color(ri, gi, bi, 0 ).texture(0f, 1f).light(light).next();
        buffer.vertex(m,  w, bottom, 0).color(ri, gi, bi, 0 ).texture(1f, 1f).light(light).next();
        buffer.vertex(m,  w, top,    0).color(ri, gi, bi, ai).texture(1f, 0f).light(light).next();
        buffer.vertex(m, -w, top,    0).color(ri, gi, bi, ai).texture(0f, 0f).light(light).next();
    }
}
