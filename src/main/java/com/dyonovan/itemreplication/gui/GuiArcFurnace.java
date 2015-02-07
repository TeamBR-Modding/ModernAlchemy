package com.dyonovan.itemreplication.gui;

import com.dyonovan.itemreplication.container.ContainerArcFurnace;
import com.dyonovan.itemreplication.helpers.GuiHelper;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.TileArcFurnaceCore;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiArcFurnace extends GuiContainer {
    private TileArcFurnaceCore core;
    private ResourceLocation background = new ResourceLocation(Constants.MODID + ":textures/gui/blastfurnace.png");

    public GuiArcFurnace(InventoryPlayer inventoryPlayer, TileArcFurnaceCore core) {
        super(new ContainerArcFurnace(inventoryPlayer, core));
        this.core = core;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        final String invTitle = "Blast Furnace";
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        fontRendererObj.drawString(invTitle, xSize / 2 - fontRendererObj.getStringWidth(invTitle) / 2, 6, 4210752);

        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); //Could do some fun colors and transparency here
        this.mc.renderEngine.bindTexture(background);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        FluidTank output = core.getOutputTank();
        FluidTank airTank = core.getAirTank();
        GuiHelper.renderFluid(output, x + 147, y + 69, 52);
        GuiHelper.renderFluid(airTank, x + 17, y + 69, 52);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        super.drawScreen(mouseX, mouseY, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        if(isInBounds(mouseX, mouseY, x + 17, y + 17, x + 32, y + 68)) {
            List<String> toolTip = new ArrayList<String>();
            toolTip.add(GuiHelper.GuiColor.YELLOW + "Compressed Air");
            toolTip.add(core.getAirTank().getFluidAmount() + "/" + core.getAirTank().getCapacity());
            renderToolTip(mouseX, mouseY, toolTip);
        }
        if(isInBounds(mouseX, mouseY, x + 147, y + 17, x + 162, y + 68)) {
            List<String> toolTip = new ArrayList<String>();
            toolTip.add(GuiHelper.GuiColor.YELLOW + "Actinium");
            toolTip.add(core.getOutputTank().getFluidAmount() + "/" + core.getOutputTank().getCapacity());
            renderToolTip(mouseX, mouseY, toolTip);
        }
    }
    /**
     * Test if location is in bounds
     * @param x xLocation
     * @param y yLocation
     * @param a Rectangle point a
     * @param b Rectangle point b
     * @param c Rectangle point c
     * @param d Rectangle point d
     * @return
     */
    public boolean isInBounds(int x, int y, int a, int b, int c, int d)
    {
        return (x >= a && x <= c && y >= b && y <=d);
    }

    public void renderToolTip(int x, int y, List<String> strings)
    {
        drawHoveringText(strings, x, y, fontRendererObj);
    }
}
