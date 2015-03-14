package com.dyonovan.modernalchemy.client.manual.component;

import java.awt.*;

public class ComponentLineBreak extends ComponentBase {

    @Override
    public void drawComponent(int x, int y, int mouseX, int mouseY) {
        drawRectangle(x + 15, y, x + 120, y + 1, new Color(255, 255, 255));
    }
}
