package com.dyonovan.modernalchemy.gui;

import com.dyonovan.modernalchemy.gui.widget.Widget;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseGui extends GuiContainer {

    protected List<Widget> widgets;

    public BaseGui(Container c) {
        super(c);
        widgets = new ArrayList<Widget>();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        for(Widget widget : widgets) {
            widget.render(guiLeft, guiTop);
        }
    }

    public void renderToolTip(int x, int y, List<String> strings)
    {
        drawHoveringText(strings, x, y, fontRendererObj);
    }
}
