package com.dyonovan.itemreplication.gui;

import com.dyonovan.itemreplication.container.BlastFurnaceContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiBlastFurnace extends GuiContainer {
    private ResourceLocation background = new ResourceLocation("textures/gui/container/furnace.png");
    public GuiBlastFurnace() {
        super(new BlastFurnaceContainer());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); //Could do some fun colors and transparency here
        this.mc.renderEngine.bindTexture(background);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
