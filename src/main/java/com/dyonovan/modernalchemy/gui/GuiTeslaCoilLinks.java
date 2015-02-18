package com.dyonovan.modernalchemy.gui;

import com.dyonovan.modernalchemy.container.ContainerTeslaCoilLinks;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class GuiTeslaCoilLinks extends BaseGui {

    private TileTeslaCoil tile;
    private ResourceLocation background = new ResourceLocation(Constants.MODID + ":textures/gui/coil_link.png");

    public GuiTeslaCoilLinks(TileTeslaCoil tileEntity) {
        super(new ContainerTeslaCoilLinks(tileEntity));

        tile = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        final String invTitle =  "Coil Links";
        fontRendererObj.drawString(invTitle, 98 - (fontRendererObj.getStringWidth(invTitle) / 2), 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        super.drawScreen(mouseX, mouseY, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
    }

    public void renderToolTip(int x, int y, List<String> strings)
    {
        drawHoveringText(strings, x, y, fontRendererObj);
    }
}
