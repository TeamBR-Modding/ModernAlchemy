package com.dyonovan.modernalchemy.client.gui;

import codechicken.nei.VisiblityData;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.TaggedInventoryArea;
import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentReverseTab;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentTabWrapperFree;
import cpw.mods.fml.common.Optional;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.api.IValueProvider;
import openmods.api.IValueReceiver;
import openmods.container.ContainerBase;
import openmods.gui.GuiConfigurableSlots;
import openmods.gui.SyncedGuiContainer;
import openmods.gui.component.*;
import openmods.gui.listener.IValueChangedListener;
import openmods.gui.logic.ValueCopyAction;
import openmods.gui.misc.IConfigurableGuiSlots;
import openmods.sync.ISyncMapProvider;
import openmods.utils.bitmap.IWriteableBitMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Optional.InterfaceList({
        @Optional.Interface(iface = "codechicken.nei.api.INEIGuiHandler", modid = "NotEnoughItems")
})
public abstract class GuiBaseConfigurableSlots<T extends TileEntity&ISyncMapProvider&IConfigurableGuiSlots<E>, C extends ContainerBase<T>, E extends Enum<E>> extends SyncedGuiContainer<C> implements INEIGuiHandler {
    protected Rectangle arrowLoc;

    public GuiBaseConfigurableSlots(C container, int width, int height, String name) {
        super(container, width, height, name);
    }

    public void setNEIArrowLocation(int x, int y, int width, int height) {
        arrowLoc = new Rectangle(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        super.drawScreen(mouseX, mouseY, par3);
        if(arrowLoc != null && mouseX >= guiLeft + arrowLoc.x && mouseX <= guiLeft + arrowLoc.x + arrowLoc.width &&
                mouseY >= guiTop + arrowLoc.y && mouseY <= guiTop + arrowLoc.y + arrowLoc.height &&
                ModernAlchemy.nei != null) {
            drawHoveringText(Arrays.asList("Recipes"), mouseX, mouseY, fontRendererObj);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button)
    {
        if (mouseX >= guiLeft - 100 && mouseX <= guiLeft && mouseY >= guiTop && mouseY <= guiTop + ySize) {
            root.mouseDown(mouseX - this.guiLeft, mouseY - this.guiTop, button);
        }

        if(arrowLoc != null && mouseX >= guiLeft + arrowLoc.x && mouseX <= guiLeft + arrowLoc.x + arrowLoc.width &&
                mouseY >= guiTop + arrowLoc.y && mouseY <= guiTop + arrowLoc.y + arrowLoc.height &&
                ModernAlchemy.nei != null) {
            ModernAlchemy.nei.onArrowClicked(this);
        }
        super.mouseClicked(mouseX, mouseY, button);

    }

    public List<GuiComponentTab> tabList;

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
        for(GuiComponentTab tab : tabList) {
            if(tab instanceof GuiComponentReverseTab)
                continue;
            if(tab.getWidth() > 24) {
                xMax += tab.getWidth() + 10;
                yMax += tab.getHeight() + 20;
            }
            else
                yMax += 24;
        }
        return ((x+w) > xMin && (x+w) < xMax && (y+h) > yMin && (y+h) < yMax) || ((x+w) < xMin + 30 && (x+w) > xMin);
    }

    protected abstract Iterable<E> getSlots();

    protected abstract void addCustomizations(BaseComposite root);

    protected abstract GuiComponentTab createTab(E slot);

    protected GuiComponentSideSelector createSideSelector(E slot, Block block, int meta, T te) {
        return new GuiComponentSideSelector(15, 15, 40.0, block, meta, te, true);
    }

    protected GuiComponentCheckbox createCheckbox(E slot) {
        return new GuiComponentCheckbox(10, 82, false, 0xFFFFFF);
    }

    protected abstract GuiComponentLabel createLabel(E slot);

    @Override
    protected final BaseComposite createRoot() {
        T te = getContainer().getOwner();

        final int meta = te.getBlockMetadata();
        final Block block = te.getBlockType();

        BaseComposite main = super.createRoot();
        addCustomizations(main);

        if(tabList == null)
            tabList = new ArrayList<>();

        GuiComponentTabWrapperFree tabs = new GuiComponentTabWrapperFree(0, 0, main);

        for (E slot : getSlots()) {
            GuiComponentTab tabTool = createTab(slot);
            tabs.addComponent(tabTool);

            GuiComponentSideSelector sideSelector = createSideSelector(slot, block, meta, te);
            GuiComponentCheckbox checkbox = createCheckbox(slot);

            setupCheckBox(checkbox, te.createAutoFlagProvider(slot), te.createAutoSlotReceiver(slot));
            setupSelector(sideSelector, te.createAllowedDirectionsProvider(slot), te.createAllowedDirectionsReceiver(slot));

            tabTool.addComponent(sideSelector);
            tabTool.addComponent(checkbox);
            tabTool.addComponent(createLabel(slot));
            tabList.add(tabTool);
        }

        for(GuiComponentTab tab : getExtraTabs()) {
            tabList.add(tab);
            tabs.addComponent(tab);
        }

        return tabs;
    }

    public abstract List<GuiComponentTab> getExtraTabs();

    private void setupSelector(GuiComponentSideSelector selector, IValueProvider<Set<ForgeDirection>> source, final IWriteableBitMap<ForgeDirection> updater) {
        selector.setListener(new GuiComponentSideSelector.ISideSelectedListener() {
            @Override
            public void onSideToggled(ForgeDirection side, boolean currentState) {
                updater.set(side, currentState);
            }
        });

        addSyncUpdateListener(ValueCopyAction.create(source, selector));
    }

    private void setupCheckBox(final GuiComponentCheckbox checkbox, IValueProvider<Boolean> source, final IValueReceiver<Boolean> updater) {
        checkbox.setListener(new IValueChangedListener<Boolean>() {
            @Override
            public void valueChanged(Boolean value) {
                updater.setValue(value);
            }
        });
        addSyncUpdateListener(ValueCopyAction.create(source, checkbox));
    }
}

