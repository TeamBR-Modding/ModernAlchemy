package com.dyonovan.modernalchemy.gui;

import com.dyonovan.modernalchemy.container.ContainerTeslaCoilLinks;
import com.dyonovan.modernalchemy.gui.buttons.ItemStackButton;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiTeslaCoilLinks extends BaseGui {

    private TileTeslaCoil tile;
    private ResourceLocation background = new ResourceLocation(Constants.MODID + ":textures/gui/coil_link.png");

    public GuiTeslaCoilLinks(TileTeslaCoil tileEntity) {
        super(new ContainerTeslaCoilLinks(tileEntity));
        tile = tileEntity;
    }

    @Override
    public void initGui() {
        super.initGui();
        guiButtons();
    }

    @SuppressWarnings("unchecked")
    protected void guiButtons() {
        int x = 135;
        int y = 54;

        if (tile.rangeMachines.size() > 0) {
            int rows = (tile.rangeMachines.size() / 7) + 1;
            int cols = tile.rangeMachines.size() / rows;
            this.buttonList.clear();

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    this.buttonList.add(new ItemStackButton(j, x, y, new ItemStack(
                            tile.getWorldObj().getBlock(tile.rangeMachines.get(i + j).x, tile.rangeMachines.get(i + j).y, tile.rangeMachines.get(i + j).z))));
                    x += 20;
                }
                x = 135;
                y += 20;
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        final String invTitle =  "Tesla Coil Machine Links";
        fontRendererObj.drawString(invTitle, (this.ySize - fontRendererObj.getStringWidth(invTitle)) / 2, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(background);
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        super.drawScreen(mouseX, mouseY, par3);
    }

    public void renderToolTip(int x, int y, List<String> strings)
    {
        drawHoveringText(strings, x, y, fontRendererObj);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

    }
}
