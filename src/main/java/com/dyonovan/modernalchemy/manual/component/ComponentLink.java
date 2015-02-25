package com.dyonovan.modernalchemy.manual.component;

import com.dyonovan.modernalchemy.helpers.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;

public class ComponentLink extends ComponentHeader {
    public ComponentLink(String label) {
        super(label);
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
        fontRenderer.drawSplitString(GuiHelper.GuiColor.BLUE + title, drawX, drawY, 120, 4210752);
        drawRectangle(x + 15, drawY + 10, x + 120, drawY + 11, new Color(255, 255, 255));
    }
}
