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
    public int getSpace() {
        return 14;
    }

    @Override
    public void drawComponent(int x, int y, int mouseX, int mouseY) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int drawX = x;
        switch(alignment) {
            case RIGHT :
                drawX += 100 - fontRenderer.getStringWidth(title);
                break;
            case CENTER :
                drawX += 67 - (fontRenderer.getStringWidth(title) / 2);
                break;
            case LEFT :
            default :
                drawX += 15;
                break;
        }
        int drawY = y;
        fontRenderer.drawSplitString(title, drawX, drawY, 120, 4210752);
        drawRectangle(x + 15, drawY + 10, x + 120, drawY + 11, new Color(255, 255, 255));
        super.drawComponent(x, y, mouseX, mouseY);
    }
}