package com.bernardo.dragoncraft.client.btn;

import com.bernardo.dragoncraft.DragonCraftZ;
import net.minecraft.util.Identifier;

public interface Btn {
    void onClick();

    int U();
    int V();
    int W();
    int H();
    int pressedU();
    int pressedV();

    Identifier BTN_TEXTURE = new Identifier(DragonCraftZ.MOD_ID, "textures/gui/icons.png");

    void render(int U, int V, int W, int H, int pressedU, int pressedV,
                int mouseX, int mouseY, boolean isPressed);
}
