package com.dyonovan.modernalchemy.client.nei.elements;

import codechicken.lib.gui.GuiDraw;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class TeslaBankElement
{
    public Rectangle position;
    public TeslaBank bank;
    public java.util.List<String> toolTip;

    public TeslaBankElement(Rectangle position, TeslaBank input, java.util.List<String> tip)
    {
        this.position = position;
        this.bank = input;
        this.toolTip = tip;
    }

    public java.util.List<String> handleTooltip (java.util.List<String> currenttip)
    {
        currenttip.add("Tesla Bank");
        currenttip.addAll(toolTip);
        return currenttip;
    }

    public void draw ()
    {
        if (this.bank == null || bank.getEnergyLevel() <= 0)
        {
            return;
        }
        GuiDraw.changeTexture(new ResourceLocation(Constants.MODID + ":textures/gui/energy.png"));

        int height = bank.getEnergyLevel() * 52 / bank.getMaxCapacity();

        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(this.position.x,               this.position.y + 52, 0,     0,                    0);
        tess.addVertexWithUV(this.position.x + 16,          this.position.y + 52, 0,     1,                    0);
        tess.addVertexWithUV(this.position.x + 16, this.position.y - height + 52, 0,     1,  (float) height / 52);
        tess.addVertexWithUV(this.position.x,      this.position.y - height + 52, 0,     0,  (float) height / 52);
        tess.draw();
    }
}
