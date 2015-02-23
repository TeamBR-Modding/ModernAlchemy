package com.dyonovan.modernalchemy.gui;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.gui.widget.Widget;
import cpw.mods.fml.common.Mod;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseGui extends GuiContainer {

    protected List<Widget> widgets;
    protected List<Zone> toolTips;
    protected Rectangle arrowLoc;
    protected Container parent;

    public BaseGui(Container c) {
        super(c);
        this.parent = c;
        widgets = new ArrayList<Widget>();
        toolTips = new ArrayList<Zone>();
    }

    public void setArrowLocation(int x, int y, int width, int height) {
        arrowLoc = new Rectangle(x, y, width, height);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        for(Widget widget : widgets) {
            widget.render(guiLeft, guiTop);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        super.drawScreen(mouseX, mouseY, par3);
        for(Zone zone : toolTips) {
            if(zone.isWithin(mouseX, mouseY))
                renderToolTip(mouseX, mouseY, zone.value);
        }
        if(mouseX >= guiLeft + arrowLoc.x && mouseX <= guiLeft + arrowLoc.x + arrowLoc.width &&
                mouseY >= guiTop + arrowLoc.y && mouseY <= guiTop + arrowLoc.y + arrowLoc.height &&
                ModernAlchemy.nei != null) {
            renderToolTip(mouseX, mouseY, "Recipes");
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button)
    {
        if(mouseX >= guiLeft + arrowLoc.x && mouseX <= guiLeft + arrowLoc.x + arrowLoc.width &&
                mouseY >= guiTop + arrowLoc.y && mouseY <= guiTop + arrowLoc.y + arrowLoc.height &&
                ModernAlchemy.nei != null) {
            ModernAlchemy.nei.onArrowClicked(parent);
        }
    }
    public void renderToolTip(int x, int y, List<String> strings)
    {
        drawHoveringText(strings, x, y, fontRendererObj);
    }

    public void renderToolTip(int x, int y, String string)
    {
        List<String> list = new ArrayList<String>();
        list.add(string);
        drawHoveringText(list, x, y, fontRendererObj);
    }

    protected class Zone {
        int x;
        int y;
        int width;
        int height;
        List<String> value;
        protected Zone(int i, int j, int w, int h, List<String> v) {
            x = i;
            y = j;
            width = w;
            height = h;
            value = v;
        }

        public boolean isWithin(int mx, int my) {
            return mx >= x && mx <= (x + width) && my <= (y + height) && my >= y;
        }
    }

}
