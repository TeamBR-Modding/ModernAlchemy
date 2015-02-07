package com.dyonovan.itemreplication.gui;

import com.dyonovan.itemreplication.container.ContainerCompressor;
import com.dyonovan.itemreplication.energy.TeslaBank;
import com.dyonovan.itemreplication.helpers.GuiHelper;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.TileCompressor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiCompressor extends GuiContainer {

    private ResourceLocation background = new ResourceLocation(Constants.MODID + ":textures/gui/compressor.png");
    private TileCompressor tile;

    public GuiCompressor(TileCompressor tile) {
        super(new ContainerCompressor(tile));
        this.tile = tile;
        this.xSize = 108;
        this.ySize = 86;
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {

        final String title1 = "Electric";
        final String title2 = "Bellows";
        fontRendererObj.drawString(title1, (108 - fontRendererObj.getStringWidth(title1)) / 2, 4, 4210752);
        fontRendererObj.drawString(title2, (108 - fontRendererObj.getStringWidth(title2)) / 2, 14, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(background);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 25, 0, xSize, ySize);

        //Render energy
        TeslaBank energyTank = tile.getEnergyBank();
        int heightTesla = energyTank.getEnergyLevel() * 52 / energyTank.getMaxCapacity();

        Tessellator tessTesla = Tessellator.instance;
        tessTesla.startDrawingQuads();
        tessTesla.addVertexWithUV(x + 12, y + 78, 0, 0.6875F, 0.35546875F);
        tessTesla.addVertexWithUV(x + 28, y + 78, 0, 0.74609375F, 0.35546875F);
        tessTesla.addVertexWithUV(x + 28, y + 78 - heightTesla, 0, 0.74609375F, (float) (91 - heightTesla) / 256);
        tessTesla.addVertexWithUV( x + 12, y + 78 - heightTesla, 0, 0.6875F, (float) (91 - heightTesla) / 256);
        tessTesla.draw();

        //Render Fluid Tank
        FluidTank tank = tile.tank;
        GuiHelper.renderFluid(tank, x + 81, y + 78, 52);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f) {
        super.drawScreen(mouseX, mouseY, f);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        //Render RF Energy Tooltip
        if(GuiHelper.isInBounds(mouseX, mouseY, x + 11, y + 26, x + 29, y + 77)) {
            List<String> toolTip = new ArrayList<String>();
            toolTip.add(GuiHelper.GuiColor.YELLOW + "Energy");
            toolTip.add(tile.getEnergyBank().getEnergyLevel() + "/" + tile.getEnergyBank().getMaxCapacity() + GuiHelper.GuiColor.ORANGE + "T");
            renderToolTip(mouseX, mouseY, toolTip);
        }
        //Render Tank Tooltip
        if(GuiHelper.isInBounds(mouseX, mouseY, x + 80, y + 26, x + 98, y + 77)) {
            List<String> toolTip = new ArrayList<String>();
            toolTip.add(GuiHelper.GuiColor.YELLOW + "Compressed Air");
            toolTip.add(tile.tank.getFluidAmount() + "/" + tile.tank.getCapacity() + GuiHelper.GuiColor.ORANGE + "mb");
            renderToolTip(mouseX, mouseY, toolTip);
        }
    }

    public void renderToolTip(int x, int y, List<String> strings)
    {
        drawHoveringText(strings, x, y, fontRendererObj);
    }
}
