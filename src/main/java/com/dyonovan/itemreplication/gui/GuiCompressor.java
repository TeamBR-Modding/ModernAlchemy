package com.dyonovan.itemreplication.gui;

import com.dyonovan.itemreplication.container.ContainerCompressor;
import com.dyonovan.itemreplication.gui.widget.WidgetEnergyBank;
import com.dyonovan.itemreplication.gui.widget.WidgetLiquidTank;
import com.dyonovan.itemreplication.helpers.GuiHelper;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.machines.TileCompressor;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiCompressor extends BaseGui {

    private ResourceLocation background = new ResourceLocation(Constants.MODID + ":textures/gui/compressor.png");
    private TileCompressor tile;

    public GuiCompressor(TileCompressor tile) {
        super(new ContainerCompressor(tile));
        this.tile = tile;
        this.xSize = 108;
        this.ySize = 86;

        widgets.add(new WidgetLiquidTank(this, this.tile.tank, 81, 78, 52));
        widgets.add(new WidgetEnergyBank(this, this.tile.getEnergyBank(), 12, 78));
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {

        final String title1 = "Electric";
        final String title2 = "Bellows";
        fontRendererObj.drawString(title1, (108 - fontRendererObj.getStringWidth(title1)) / 2, 4, 4210752);
        fontRendererObj.drawString(title2, (108 - fontRendererObj.getStringWidth(title2)) / 2, 14, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(background);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 25, 0, xSize, ySize);

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
}
