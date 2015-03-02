package com.dyonovan.modernalchemy.gui;

import com.dyonovan.modernalchemy.container.ContainerAdvancedCrafter;
import com.dyonovan.modernalchemy.gui.buttons.MultiStateButton;
import com.dyonovan.modernalchemy.handlers.PacketHandler;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.network.ModeSwitchPacket;
import com.dyonovan.modernalchemy.tileentity.machines.TileAdvancedCrafter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiAdvancedCrafter extends BaseGui {

    private TileAdvancedCrafter tile;
    private ResourceLocation background = new ResourceLocation(Constants.MODID + ":textures/gui/ma_furnace.png");
    int x, y;
    MultiStateButton modeButton;

    public GuiAdvancedCrafter(InventoryPlayer inventoryPlayer, TileAdvancedCrafter tileEntity) {
        super(new ContainerAdvancedCrafter(inventoryPlayer, tileEntity));

        this.tile = tileEntity;
        setArrowLocation(108, 35, 24, 16);

        //widgets.add(new WidgetPulse(this, tile, 44, 52));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        this.x = (width - xSize) / 2;
        this.y = (height - ySize) / 2;
        modeButton = new MultiStateButton(0, x + 38, y + 35, 16, 16, 3, "textures/gui/AC_states.png");
        modeButton.setCurrentState(tile.currentMode);
        this.buttonList.add(modeButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                modeButton.cycleIcon();
                PacketHandler.net.sendToServer(new ModeSwitchPacket.UpdateMessage(
                        modeButton.getState(), tile.xCoord, tile.yCoord, tile.zCoord));
                break;
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        final String invTitle =  "Advanced Crafter";
        final String modeTitle = "Mode";
        fontRendererObj.drawString(invTitle, (fontRendererObj.getStringWidth(invTitle) / 2), 6, 4210752);
        fontRendererObj.drawString(modeTitle, 34, 26, 4210752);
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 95, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); //Could do some fun colors and transparency here
        this.mc.renderEngine.bindTexture(background);
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        //Render RF energy
        int heightRF = tile.getEnergyStored(null) * 52 / tile.getMaxEnergyStored(null);

        Tessellator tessRF = Tessellator.instance;
        tessRF.startDrawingQuads();
        tessRF.addVertexWithUV(x + 8, y + 78, 0, 0.6875F, 0.35546875F);
        tessRF.addVertexWithUV(x + 24, y + 78, 0, 0.75F, 0.35546875F);
        tessRF.addVertexWithUV(x + 24, y + 78 - heightRF, 0, 0.75F, (float) (91 - heightRF) / 256); //256);
        tessRF.addVertexWithUV(x + 8, y + 78 - heightRF, 0, 0.6875F, (float) (91 - heightRF) / 256);
        tessRF.draw();

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
        if (GuiHelper.isInBounds(mouseX, mouseY, x + 8, y + 26, x + 23, y + 77)) {
            List<String> toolTip = new ArrayList<String>();
            toolTip.add(GuiHelper.GuiColor.YELLOW + "Energy");
            toolTip.add(tile.getEnergyStored(null) + "/" + tile.getMaxEnergyStored(null) + GuiHelper.GuiColor.RED + "RF");
            renderToolTip(mouseX, mouseY, toolTip);
        }
        if (GuiHelper.isInBounds(mouseX, mouseY, x + 38, y + 35, x + 54, y + 93)) {
            List<String> toolTip = new ArrayList<String>();
            switch (modeButton.getState()) {
                case 0:
                    toolTip.add("Enriching");
                    break;
                case 1:
                    toolTip.add("Extruding");
                    break;
                case 2:
                    toolTip.add("Bending");
                    break;
                default:
                    toolTip.add("Something is broken!");
            }
            renderToolTip(mouseX, mouseY, toolTip);
        }
    }

    public void renderToolTip(int x, int y, List<String> strings)
    {
        drawHoveringText(strings, x, y, fontRendererObj);
    }

}
