package com.dyonovan.modernalchemy.client.manual.pages;

import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiButtonManual extends GuiButton {

    private ResourceLocation manualGui = new ResourceLocation(Constants.MODID + ":textures/gui/manual/manual.png");
    private int id;

    public GuiButtonManual(int index, int x, int y, int type) {
        super(index, x, y, 18, 10, "");
        id = type;
    }

    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.visible)
        {
            par1Minecraft.getTextureManager().bindTexture(manualGui);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;

            int u = 0;
            int v = 175 + (13 * id);
            if (flag)
            {
                u += 23;

            }
            this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, this.width, this.height);
        }
    }
}
