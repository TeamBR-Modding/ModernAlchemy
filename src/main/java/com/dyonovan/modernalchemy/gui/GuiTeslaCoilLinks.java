package com.dyonovan.modernalchemy.gui;

import com.dyonovan.modernalchemy.container.ContainerTeslaCoilLinks;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.util.Location;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiTeslaCoilLinks extends BaseGui {

    private TileTeslaCoil tile;
    private ResourceLocation background = new ResourceLocation(Constants.MODID + ":textures/gui/coil_link.png");
    private int high, low;


    public GuiTeslaCoilLinks(TileTeslaCoil tileEntity) {
        super(new ContainerTeslaCoilLinks(tileEntity));
        tile = tileEntity;
        low = 0;
        high = tile.link.size() > 9 ? 10 : tile.link.size();
    }

    @Override
    public void initGui() {
        super.initGui();
        guiButtons();
    }

    protected void guiButtons() {
        int x = 60;
        this.buttonList.clear();
        for (int i = 0; i < high; i++) {
            String string = (new ArrayList<String>(tile.link.keySet())).get(i);
            Location loc = (new ArrayList<Location>(tile.link.values())).get(i);
            String list = string + " - " + Integer.toString(loc.x) + Integer.toString(loc.y) + Integer.toString(loc.z);
            this.buttonList.add(new GuiButton(i, 135, x, 150, 12, list));
            x += 15;
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        final String invTitle =  "Coil Links";
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
