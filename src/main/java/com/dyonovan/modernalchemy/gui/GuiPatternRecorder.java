package com.dyonovan.modernalchemy.gui;

import com.dyonovan.modernalchemy.container.ContainerPatternRecorder;
import com.dyonovan.modernalchemy.gui.widgets.WidgetEnergyBank;
import com.dyonovan.modernalchemy.gui.widgets.WidgetPulse;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.machines.TilePatternRecorder;
import com.dyonovan.teambrcore.gui.BaseGui;
import com.dyonovan.teambrcore.helpers.GuiHelper;
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
        widgets.add(new WidgetPulse(this, tile, 77, 80));
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
