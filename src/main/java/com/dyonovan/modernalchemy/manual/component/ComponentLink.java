package com.dyonovan.modernalchemy.manual.component;

import com.dyonovan.modernalchemy.helpers.GuiHelper;
import com.dyonovan.modernalchemy.manual.page.ManualPages;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;

public class ComponentLink extends ComponentBase {
    protected String title;
    protected String destination;

    public ComponentLink(String label, String dest, ALIGNMENT alignment) {
        setAlignment(alignment);
        title = label;
        destination = dest;
        addToTip(GuiHelper.GuiColor.YELLOW + "Click To Open Details");
    }

    @Override
    public int getSpace() {
        return (((Minecraft.getMinecraft().fontRenderer.getStringWidth(title) / 110) + 1) * 11);
    }

    @Override
    public void onMouseLeftClick() {
        if(ManualPages.instance.getPage(destination) != null)
            ManualPages.instance.openPage(ManualPages.instance.getPage(destination));
        super.onMouseLeftClick();
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
        fontRenderer.drawSplitString(GuiHelper.GuiColor.BLUE + title, drawX, drawY, 110, 4210752);
        super.drawComponent(x, y, mouseX, mouseY);
    }
}
