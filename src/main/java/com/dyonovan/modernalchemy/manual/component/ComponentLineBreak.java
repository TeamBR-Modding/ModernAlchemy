package com.dyonovan.modernalchemy.manual.component;

import java.awt.*;

public class ComponentLineBreak extends ComponentBase {

    @Override
    public int getSpace() {
        return 4;
    }

    @Override
    public void drawComponent(int x, int y, int mouseX, int mouseY) {
        drawRectangle(x + 15, y, x + 120, y + 1, new Color(255, 255, 255));
    }
}
