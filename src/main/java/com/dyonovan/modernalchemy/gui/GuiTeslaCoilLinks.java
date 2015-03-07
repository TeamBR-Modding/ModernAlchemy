package com.dyonovan.modernalchemy.gui;

import com.dyonovan.modernalchemy.container.ContainerTeslaCoilLinks;
import com.dyonovan.modernalchemy.gui.buttons.ItemStackButton;
import com.dyonovan.modernalchemy.handlers.PacketHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.network.UpdateServerCoilLists;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.util.Location;
import com.dyonovan.teambrcore.gui.BaseGui;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiTeslaCoilLinks extends BaseGui {

    private TileTeslaCoil tile;
    private ResourceLocation background = new ResourceLocation(Constants.MODID + ":textures/gui/coil_link.png");
    private int index1;

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
        int x = guiLeft + 8;
        int y = guiTop + 16;
        index1 = 0;
        int index2 = 0;
        toolTips.clear();
        this.buttonList.clear();

        //TODO Deal with more then 24 machines

        if (tile.rangeMachines.size() > 0) {
            int rows = (int) Math.ceil(tile.rangeMachines.size() / 8.0);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < 8; j++) {
                    if (tile.rangeMachines.size() > index1) {
                        Block block = tile.getWorldObj().getBlock(tile.rangeMachines.get(index1).x, tile.rangeMachines.get(index1).y, tile.rangeMachines.get(index1).z);
                        this.buttonList.add(new ItemStackButton(index1, x, y, new ItemStack(block)));
                        List<String> string = new ArrayList<String>();
                        string.add(block.getLocalizedName());
                        string.add("X: " + tile.rangeMachines.get(index1).x);
                        string.add("Y: " + tile.rangeMachines.get(index1).y);
                        string.add("Z: " + tile.rangeMachines.get(index1).z);
                        toolTips.add(new Zone(x, y, 20, 20, string));
                        x += 20;
                        index1 += 1;
                    }
                }
                x = 133;
                y += 20;
            }
        }

        x = guiLeft + 8;
        y = guiTop + 95;
        if (tile.linkedMachines.size() > 0) {
            int rows = (int) Math.ceil(tile.linkedMachines.size() / 8.0);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < 8; j++) {
                    if (tile.linkedMachines.size() > index2) {
                        Block block = tile.getWorldObj().getBlock(tile.linkedMachines.get(index2).x, tile.linkedMachines.get(index2).y, tile.linkedMachines.get(index2).z);
                        this.buttonList.add(new ItemStackButton(index1 + index2, x, y, new ItemStack(block)));
                        List<String> string = new ArrayList<String>();
                        string.add(block.getLocalizedName());
                        string.add("X: " + tile.linkedMachines.get(index2).x);
                        string.add("Y: " + tile.linkedMachines.get(index2).y);
                        string.add("Z: " + tile.linkedMachines.get(index2).z);
                        toolTips.add(new Zone(x, y, 20, 20, string));
                        x += 20;
                        index2 += 1;
                    }
                }
                x = 133;
                y += 20;
            }
        }
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        for(Object button1 : buttonList) {
            GuiButton guiButton = (GuiButton)button1;
            if(x >= guiButton.xPosition && x <= guiButton.xPosition + 20 && y >= guiButton.yPosition && y <= guiButton.yPosition + 20) {
                GuiScreenEvent.ActionPerformedEvent.Pre event = new GuiScreenEvent.ActionPerformedEvent.Pre(this, guiButton, this.buttonList);
                if (MinecraftForge.EVENT_BUS.post(event))
                    break;
                event.button.func_146113_a(this.mc.getSoundHandler());
                this.actionPerformed(event.button);
                if (this.equals(this.mc.currentScreen))
                    MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.ActionPerformedEvent.Post(this, event.button, this.buttonList));

                return;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id < index1 && tile.rangeMachines.size() > 0) {
            tile.linkedMachines.add(new Location(tile.rangeMachines.get(button.id).x,
                    tile.rangeMachines.get(button.id).y, tile.rangeMachines.get(button.id).z));
            tile.rangeMachines.remove(button.id);
        } else if (tile.rangeMachines.size() == 0 || button.id >= index1) {
            tile.rangeMachines.add(new Location(tile.linkedMachines.get(button.id - index1).x,
                    tile.linkedMachines.get(button.id - index1).y, tile.linkedMachines.get(button.id - index1).z));
            tile.linkedMachines.remove(button.id - index1);
        }

        PacketHandler.net.sendToServer(new UpdateServerCoilLists.UpdateMessage(tile.xCoord, tile.yCoord, tile.zCoord,
                "linkedMachines", tile.linkedMachines));
        PacketHandler.net.sendToServer(new UpdateServerCoilLists.UpdateMessage(tile.xCoord, tile.yCoord, tile.zCoord,
                "rangeMachines", tile.rangeMachines));
        guiButtons();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String invTitle =  "Tesla Coil Machine Links";
        fontRendererObj.drawString(invTitle, (this.ySize - fontRendererObj.getStringWidth(invTitle)) / 2, 6, 4210752);
        invTitle = "Tesla Coil Machine Links";
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
