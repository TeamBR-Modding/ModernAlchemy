package com.dyonovan.itemreplication.gui;

import com.dyonovan.itemreplication.container.ContainerPatternRecorder;
import com.dyonovan.itemreplication.gui.widget.WidgetEnergyBank;
import com.dyonovan.itemreplication.helpers.GuiHelper;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.machines.TilePatternRecorder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiPatternRecorder extends BaseGui {

    private TilePatternRecorder tile;
    private ResourceLocation background = new ResourceLocation(Constants.MODID + ":textures/gui/patternrecorder.png");

    public GuiPatternRecorder(InventoryPlayer playerInventory, TilePatternRecorder tileEntity){
        super(new ContainerPatternRecorder(playerInventory, tileEntity));
        tile = tileEntity;

        widgets.add(new WidgetEnergyBank(this, tile.getEnergyBank(), 8, 78));

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        final String invTitle = "Pattern Recorder";
        fontRendererObj.drawString(invTitle, 56, 12, 4210752);
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 119, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); //Could do some fun colors and transparency here
        this.mc.renderEngine.bindTexture(background);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        /*//Render energy
        TeslaBank energyTank = tile.getEnergyTank();
        int height = energyTank.getEnergyLevel() * 52 / energyTank.getMaxCapacity();

        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(x + 8,           y + 78, 0,     0.6875F,                 0.35546875F);
        tess.addVertexWithUV(x + 24,          y + 78, 0, 0.74609375F,                 0.35546875F);
        tess.addVertexWithUV(x + 24, y + 78 - height, 0, 0.74609375F, (float) (91 - height) / 256);
        tess.addVertexWithUV(x + 8,  y + 78 - height, 0,     0.6875F, (float) (91 - height) / 256);
        tess.draw();*/

        //Render pulse
        Tessellator tess = Tessellator.instance;
        GL11.glPushMatrix();
        tess.startDrawingQuads();
        GL11.glTranslatef(10, 26, 0); // offset the pulse because we are drawing it in a different location
        tess.addVertexWithUV(x + 66, y + 53, 0,     0.6875F, 0.08203125F);
        tess.addVertexWithUV(x + 93, y + 53, 0, 0.78515625F, 0.08203125F);
        tess.addVertexWithUV(x + 93, y + 32, 0, 0.78515625F,        0.0F);
        tess.addVertexWithUV(x + 66, y + 32, 0,     0.6875F,        0.0F);
        tess.draw();
        GL11.glPopMatrix();

        //Draw Arrow
        int arrow = tile.getProgressScaled(24);
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
            toolTip.add(tile.getEnergyLevel() + "/" + tile.getEnergyBank().getMaxCapacity() + GuiHelper.GuiColor.ORANGE + "T");
            renderToolTip(mouseX, mouseY, toolTip);
        }
    }

    public void renderToolTip(int x, int y, List<String> strings)
    {
        drawHoveringText(strings, x, y, fontRendererObj);
    }
}
