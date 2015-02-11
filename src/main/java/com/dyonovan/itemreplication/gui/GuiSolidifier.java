package com.dyonovan.itemreplication.gui;

import com.dyonovan.itemreplication.container.ContainerSolidifier;
import com.dyonovan.itemreplication.energy.TeslaBank;
import com.dyonovan.itemreplication.gui.widget.WidgetEnergyBank;
import com.dyonovan.itemreplication.gui.widget.WidgetLiquidTank;
import com.dyonovan.itemreplication.helpers.GuiHelper;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.TileSolidifier;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiSolidifier extends BaseGui  {

    private ResourceLocation background = new ResourceLocation(Constants.MODID + ":textures/gui/solidifier.png");
    private TileSolidifier tile;

    public GuiSolidifier(InventoryPlayer inventory, TileSolidifier tile) {
        super(new ContainerSolidifier(inventory, tile));
        this.tile = tile;

        widgets.add(new WidgetLiquidTank(this, tile.tank, 37, 78, 52));
        widgets.add(new WidgetEnergyBank(this, tile.getEnergyBank(), 8, 78));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        final String invTitle =  "Amalgamator";
        fontRendererObj.drawString(invTitle, 98 - (fontRendererObj.getStringWidth(invTitle) / 2), 6, 4210752);
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 72, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); //Could do some fun colors and transparency here
        this.mc.renderEngine.bindTexture(background);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        //Draw Arrow
        int arrow = tile.getCookTimeScaled(24);
        drawTexturedModalRect(x + 107, y + 35, 176, 22, arrow, 17);

        super.drawGuiContainerBackgroundLayer(f, i, j);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        super.drawScreen(mouseX, mouseY, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        if(GuiHelper.isInBounds(mouseX, mouseY, x + 8, y + 26, x + 23, y + 77)) {
            List<String> toolTip = new ArrayList<String>();
            toolTip.add(GuiHelper.GuiColor.YELLOW + "Energy");
            toolTip.add(tile.getEnergyBank().getEnergyLevel() + "/" + tile.getEnergyBank().getMaxCapacity() + GuiHelper.GuiColor.ORANGE + "T");
            renderToolTip(mouseX, mouseY, toolTip);
        }
        if(GuiHelper.isInBounds(mouseX, mouseY, x + 37, y + 26, x + 52, y + 77)) {
            List<String> toolTip = new ArrayList<String>();
            toolTip.add(GuiHelper.GuiColor.YELLOW + "Actinium");
            toolTip.add(tile.tank.getFluidAmount() + "/" + tile.tank.getCapacity() + GuiHelper.GuiColor.ORANGE + "mb");
            renderToolTip(mouseX, mouseY, toolTip);
        }
    }

    public void renderToolTip(int x, int y, List<String> strings)
    {
        drawHoveringText(strings, x, y, fontRendererObj);
    }
}
