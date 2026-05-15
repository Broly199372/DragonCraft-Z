package com.bernardo.dragoncraft.client.btn;

public class BtnDelete implements Btn {

    private final Runnable action;
    private final boolean locked;

    public BtnDelete(Runnable action, boolean locked) {
        this.action = action;
        this.locked = locked;
    }

    @Override public void onClick()     { if (!locked) action.run(); }

    @Override public int U()            { return locked ? 97 : 77; }
    @Override public int V()            { return 0;  }
    @Override public int W()            { return 20; }
    @Override public int H()            { return 20; }
    @Override public int pressedU()     { return 77; }
    @Override public int pressedV()     { return 20; }

    @Override
    public void render(int U, int V, int W, int H, int pressedU, int pressedV,
                       int mouseX, int mouseY, boolean isPressed) {
        int renderU = locked ? U() : (isPressed ? pressedU() : U());
        int renderV = locked ? V() : (isPressed ? pressedV() : V());
    }
}
