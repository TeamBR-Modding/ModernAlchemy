package com.dyonovan.modernalchemy.client.gui.widgets;

import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.client.gui.widget.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class WidgetEnergyBank extends Widget {

    private TeslaBank bank;

    /**
     * @param gui Parent Gui
     * @param x   How far from guiLeft
     * @param y   How far down from guiTop
     */
    public WidgetEnergyBank(Gui gui, TeslaBank teslaBank, int x, int y) {
        super(gui, x, y);
        bank = teslaBank;
    }

    @Override
    public void render(int x, int y) {
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Constants.MODID + ":textures/gui/energy.png"));
        int height = bank.getEnergyLevel() * 52 / bank.getMaxCapacity();

        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(x + xPos,               yPos + y, 0,     0,                    0);
        tess.addVertexWithUV(x + xPos + 16,          yPos + y, 0,     1,                    0);
        tess.addVertexWithUV(x + xPos + 16, y + yPos - height, 0,     1,  (float) height / 52);
        tess.addVertexWithUV(x + xPos,      y + yPos - height, 0,     0,  (float) height / 52);
        tess.draw();
    }
}
