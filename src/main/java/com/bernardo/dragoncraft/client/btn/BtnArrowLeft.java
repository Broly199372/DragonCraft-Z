package com.bernardo.dragoncraft.client.btn;

public class BtnArrowLeft implements Btn {

    private final Runnable action;

    public BtnArrowLeft(Runnable action) {
        this.action = action;
    }

    @Override public void onClick()     { action.run(); }

    @Override public int U()            { return 0;  }
    @Override public int V()            { return 0;  }
    @Override public int W()            { return 20; }
    @Override public int H()            { return 20; }
    @Override public int pressedU()     { return 0;  }
    @Override public int pressedV()     { return 20; }

    @Override
    public void render(int U, int V, int W, int H, int pressedU, int pressedV,
                       int mouseX, int mouseY, boolean isPressed) {
        int renderU = isPressed ? pressedU() : U();
        int renderV = isPressed ? pressedV() : V();
    }
}
