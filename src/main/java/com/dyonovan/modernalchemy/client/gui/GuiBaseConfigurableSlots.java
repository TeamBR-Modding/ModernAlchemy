package com.dyonovan.modernalchemy.client.gui;

import codechicken.nei.VisiblityData;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.TaggedInventoryArea;
import com.dyonovan.teambrcore.TeamBRCore;
import cpw.mods.fml.common.Optional;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import openmods.container.ContainerBase;
import openmods.gui.GuiConfigurableSlots;
import openmods.gui.component.GuiComponentTab;
import openmods.gui.misc.IConfigurableGuiSlots;
import openmods.sync.ISyncMapProvider;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

@Optional.InterfaceList({
        @Optional.Interface(iface = "codechicken.nei.api.INEIGuiHandler", modid = "NotEnoughItems")
})
public abstract class GuiBaseConfigurableSlots<T extends TileEntity&ISyncMapProvider&IConfigurableGuiSlots<E>, C extends ContainerBase<T>, E extends Enum<E>> extends GuiConfigurableSlots<T, C, E> implements INEIGuiHandler {
    protected Rectangle arrowLoc;

    public GuiBaseConfigurableSlots(C container, int width, int height, String name) {
        super(container, width, height, name);
    }

    public void setArrowLocation(int x, int y, int width, int height) {
        arrowLoc = new Rectangle(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        super.drawScreen(mouseX, mouseY, par3);
        if(arrowLoc != null && mouseX >= guiLeft + arrowLoc.x && mouseX <= guiLeft + arrowLoc.x + arrowLoc.width &&
                mouseY >= guiTop + arrowLoc.y && mouseY <= guiTop + arrowLoc.y + arrowLoc.height &&
                TeamBRCore.nei != null) {
            drawHoveringText(Arrays.asList("Recipes"), mouseX, mouseY, fontRendererObj);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button)
    {
        if(arrowLoc != null && mouseX >= guiLeft + arrowLoc.x && mouseX <= guiLeft + arrowLoc.x + arrowLoc.width &&
                mouseY >= guiTop + arrowLoc.y && mouseY <= guiTop + arrowLoc.y + arrowLoc.height &&
                TeamBRCore.nei != null) {
            TeamBRCore.nei.onArrowClicked(getContainer());
        }
        super.mouseClicked(mouseX, mouseY, button);
    }
    public List<GuiComponentTab> tabs;

    @Override
    public VisiblityData modifyVisiblity(GuiContainer guiContainer, VisiblityData visiblityData) {
        return null;
    }

    @Override
    public Iterable<Integer> getItemSpawnSlots(GuiContainer guiContainer, ItemStack itemStack) {
        return null;
    }

    @Override
    public List<TaggedInventoryArea> getInventoryAreas(GuiContainer guiContainer) {
        return null;
    }

    @Override
    public boolean handleDragNDrop(GuiContainer guiContainer, int i, int i1, ItemStack itemStack, int i2) {
        return false;
    }

    @Override
    @Optional.Method(modid = "NotEnoughItems")
    public boolean hideItemPanelSlot(GuiContainer gc, int x, int y, int w, int h) {
        int xMin = guiLeft + xSize;
        int yMin = guiTop;
        int xMax = xMin;
        int yMax = yMin;
        for(GuiComponentTab tab : tabs) {
            if(tab.getWidth() > 24) {
                xMax += tab.getWidth() + 10;
                yMax += tab.getHeight() + 20;
            }
            else
                yMax += 24;
        }
        return ((x+w) > xMin && (x+w) < xMax && (y+h) > yMin && (y+h) < yMax) || ((x+w) < xMin + 30 && (x+w) > xMin);
    }
}
