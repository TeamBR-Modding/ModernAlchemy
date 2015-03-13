package com.dyonovan.modernalchemy.gui;

import codechicken.nei.VisiblityData;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.TaggedInventoryArea;
import cpw.mods.fml.common.Optional;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import openmods.container.ContainerBase;
import openmods.gui.GuiConfigurableSlots;
import openmods.gui.component.GuiComponentTab;
import openmods.gui.misc.IConfigurableGuiSlots;
import openmods.sync.ISyncMapProvider;

import java.util.ArrayList;
import java.util.List;

@Optional.InterfaceList({
        @Optional.Interface(iface = "codechicken.nei.api.INEIGuiHandler", modid = "NotEnoughItems")
})
public abstract class GuiBaseConfigurableSlots<T extends TileEntity&ISyncMapProvider&IConfigurableGuiSlots<E>, C extends ContainerBase<T>, E extends Enum<E>> extends GuiConfigurableSlots<T, C, E> implements INEIGuiHandler {
    public GuiBaseConfigurableSlots(C container, int width, int height, String name) {
        super(container, width, height, name);
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
