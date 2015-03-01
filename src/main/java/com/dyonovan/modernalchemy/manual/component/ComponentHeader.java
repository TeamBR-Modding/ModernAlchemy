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
        int drawX = xPos;
        switch(alignment) {
            case RIGHT :
                drawX += 85 - fontRenderer.getStringWidth(title);
                break;
            case CENTER :
                drawX += 53 - (fontRenderer.getStringWidth(title) / 2);
                break;
            case LEFT :
            default :
                drawX += 0;
                break;
        }
        int drawY = yPos;
        fontRenderer.drawSplitString(title, x + drawX, y + drawY, 120, 4210752);
        drawRectangle(x + xPos, y + yPos + 10, x + xPos + 105, y + yPos + 11, new Color(255, 255, 255));
        super.drawComponent(x, y, mouseX, mouseY);
    }
}