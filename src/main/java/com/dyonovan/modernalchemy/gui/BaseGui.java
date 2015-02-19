package com.dyonovan.modernalchemy.gui;

import com.dyonovan.modernalchemy.gui.widget.Widget;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseGui extends GuiContainer {

    protected List<Widget> widgets;
    protected List<Zone> toolTips;

    public BaseGui(Container c) {
        super(c);
        widgets = new ArrayList<Widget>();
        toolTips = new ArrayList<Zone>();
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
    }

    public void renderToolTip(int x, int y, List<String> strings)
    {
        drawHoveringText(strings, x, y, fontRendererObj);
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
