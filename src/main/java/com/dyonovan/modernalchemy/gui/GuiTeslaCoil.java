package com.dyonovan.modernalchemy.gui;

import com.dyonovan.modernalchemy.container.ContainerTeslaCoil;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.gui.widget.WidgetEnergyBank;
import com.dyonovan.modernalchemy.gui.widget.WidgetPulse;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiTeslaCoil extends BaseGui {

    private ResourceLocation background = new ResourceLocation(Constants.MODID + ":textures/gui/tesla_coil.png");
    private TileTeslaCoil tile;

    public GuiTeslaCoil(TileTeslaCoil tile) {
        super(new ContainerTeslaCoil(tile));
        this.tile = tile;

        this.xSize = 108;
        this.ySize = 86;

        widgets.add(new WidgetPulse(this, tile, 79, 42));
        widgets.add((new WidgetEnergyBank(this, tile.getEnergyBank(), 81, 78)));
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {

        final String title1 = "Tesla";
        final String title2 = "Coil";
        fontRendererObj.drawString(title1, (108 - fontRendererObj.getStringWidth(title1)) / 2, 4, 4210752);
        fontRendererObj.drawString(title2, (108 - fontRendererObj.getStringWidth(title2)) / 2, 14, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); //Could do some fun colors and transparency here
        this.mc.renderEngine.bindTexture(background);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 25, 0, xSize, ySize);

        //Render RF energy
        int heightRF = tile.getRFEnergyStored() * 52 / tile.getRFMaxEnergyStored();

        Tessellator tessRF = Tessellator.instance;
        tessRF.startDrawingQuads();
        tessRF.addVertexWithUV(x + 12, y + 78, 0, 0.625F, 0.35546875F);
        tessRF.addVertexWithUV(x + 28, y + 78, 0, 0.6875F, 0.35546875F);
        tessRF.addVertexWithUV(x + 28, y + 78 - heightRF, 0, 0.6875F, (float) (91 - heightRF) / 256);
        tessRF.addVertexWithUV(x + 12, y + 78 - heightRF, 0, 0.625F, (float) (91 - heightRF) / 256);
        tessRF.draw();

        /*//Render energy
        TeslaBank energyTank = tile.getEnergyBank();
        int heightTesla = energyTank.getEnergyLevel() * 52 / energyTank.getMaxCapacity();

        Tessellator tessTesla = Tessellator.instance;
        tessTesla.startDrawingQuads();
        tessTesla.addVertexWithUV(x + 81, y + 78, 0, 0.6875F, 0.35546875F);
        tessTesla.addVertexWithUV(x + 97, y + 78, 0, 0.74609375F, 0.35546875F);
        tessTesla.addVertexWithUV(x + 97, y + 78 - heightTesla, 0, 0.74609375F, (float) (91 - heightTesla) / 256);
        tessTesla.addVertexWithUV( x + 81, y + 78 - heightTesla, 0, 0.6875F, (float) (91 - heightTesla) / 256);
        tessTesla.draw();*/
        super.drawGuiContainerBackgroundLayer(f, i, j);

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
            toolTip.add(tile.getRFEnergyStored() + "/" + tile.getRFMaxEnergyStored() + GuiHelper.GuiColor.ORANGE + "RF");
            renderToolTip(mouseX, mouseY, toolTip);
        }
        //Render Tesla Energy Tooltip
        if(GuiHelper.isInBounds(mouseX, mouseY, x + 80, y + 26, x + 98, y + 77)) {
            List<String> toolTip = new ArrayList<String>();
            toolTip.add(GuiHelper.GuiColor.YELLOW + "Energy");
            toolTip.add(tile.getEnergyBank().getEnergyLevel() + "/" + tile.getEnergyBank().getMaxCapacity() + GuiHelper.GuiColor.ORANGE + "T");
            renderToolTip(mouseX, mouseY, toolTip);
        }
    }

    public void renderToolTip(int x, int y, List<String> strings)
    {
        drawHoveringText(strings, x, y, fontRendererObj);
    }
}
