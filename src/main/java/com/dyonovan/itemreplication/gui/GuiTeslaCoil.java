package com.dyonovan.itemreplication.gui;

import com.dyonovan.itemreplication.container.ContainerTeslaCoil;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.TileTeslaCoil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiTeslaCoil extends GuiContainer {

    private ResourceLocation background = new ResourceLocation(Constants.MODID + ":textures/gui/tesla_coil");
    private TileTeslaCoil tile;

    public GuiTeslaCoil(TileTeslaCoil tile) {
        super(new ContainerTeslaCoil(tile));
        this.tile = tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); //Could do some fun colors and transparency here
        this.mc.renderEngine.bindTexture(background);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f) {
        super.drawScreen(mouseX, mouseY, f);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
    }
}
