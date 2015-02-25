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
    public int getSpace() {
        return ((Minecraft.getMinecraft().fontRenderer.getStringWidth(textField) / 110) * 11);
    }

    @Override
    public void drawComponent(int x, int y, int mouseX, int mouseY) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int drawX = x + 15;
        int drawY = y;
        fontRenderer.drawSplitString(textField, drawX, drawY, 110, 4210752);
    }
}