package com.dyonovan.itemreplication.gui.widget;

import com.dyonovan.itemreplication.helpers.GuiHelper;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fluids.FluidTank;

public class WidgetLiquidTank extends Widget {

    private FluidTank tank;
    private int max;

    public WidgetLiquidTank(Gui gui, FluidTank fluidTank, int x, int y, int height) {
        super(gui, x, y);
        tank = fluidTank;
        max = height;
    }

    @Override
    public void render(int x, int y) {
        GuiHelper.renderFluid(tank, x + xPos, y + yPos, max);
    }
}
