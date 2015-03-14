package com.dyonovan.modernalchemy.client.manual.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;
import java.util.ArrayList;

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

        ArrayList<String> strings = new ArrayList<String>();
        strings.add("");
        String[] words = title.split(" ");
        int pos = 0;
        int width = 0;
        for(String string : words) {
            if(fontRenderer.getStringWidth(string) + width < 120) {
                strings.set(pos, strings.get(pos) + " " + string);
                width = fontRenderer.getStringWidth(strings.get(pos));
            }
            else {
                strings.add(" " + string);
                width = fontRenderer.getStringWidth(string);
                pos++;
            }
        }
        int i = 0;
        for(String string : strings) {
            int drawX = xPos;
            switch (alignment) {
                case RIGHT:
                    drawX += 85 - fontRenderer.getStringWidth(string);
                    break;
                case CENTER:
                    drawX += 53 - (fontRenderer.getStringWidth(string) / 2);
                    break;
                case LEFT:
                default:
                    drawX += 0;
                    break;
            }
            int drawY = yPos + (10 * i);
            fontRenderer.drawSplitString(string, x + drawX, y + drawY, 120, 4210752);
            i++;
        }
        drawRectangle(x + xPos, y + yPos + 10 + (10 * pos), x + xPos + 105, y + yPos + 11 + (10 * pos), new Color(255, 255, 255));
        super.drawComponent(x, y, mouseX, mouseY);
    }
}