package com.dyonovan.modernalchemy.manual.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;

public class ComponentHeader extends ComponentBase {
    protected String title;

    public ComponentHeader(String label) {
        title = label;
    }

    public void setTitle(String string) {
        title = string;
        title.trim();
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void drawComponent(int x, int y, int mouseX, int mouseY) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        fontRenderer.drawSplitString(title, x + xPos, y + yPos, 120, 4210752);
        drawRectangle(x + xPos, y + yPos, x + 120, y + 11, new Color(255, 255, 255));
        super.drawComponent(x, y, mouseX, mouseY);
    }
}