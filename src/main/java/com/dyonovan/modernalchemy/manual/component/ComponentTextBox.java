package com.dyonovan.modernalchemy.manual.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;

public class ComponentTextBox extends ComponentBase {
    private String textField;

    public ComponentTextBox(String label) {
        textField = label;
    }

    @Override
    public void drawComponent(int x, int y, int mouseX, int mouseY) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int drawX = x + xPos;
        int drawY = y + yPos;
        fontRenderer.drawSplitString(textField, drawX, drawY, 110, 4210752);
    }
}