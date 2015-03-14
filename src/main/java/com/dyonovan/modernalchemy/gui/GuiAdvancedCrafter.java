package com.dyonovan.modernalchemy.gui;

import com.dyonovan.modernalchemy.container.ContainerAdvancedCrafter;
import com.dyonovan.modernalchemy.gui.components.GuiComponentArrowProgress;
import com.dyonovan.modernalchemy.gui.components.GuiComponentChangeIconButton;
import com.dyonovan.modernalchemy.gui.components.GuiComponentRF;
import com.dyonovan.modernalchemy.gui.components.GuiComponentToolTip;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.rpc.ILevelChanger;
import com.dyonovan.modernalchemy.tileentity.machines.TileAdvancedCrafter;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import openmods.gui.GuiConfigurableSlots;
import openmods.gui.component.*;
import openmods.gui.listener.IMouseDownListener;
import openmods.gui.logic.ValueCopyAction;
import openmods.utils.MiscUtils;
import openmods.utils.render.FakeIcon;

public class GuiAdvancedCrafter extends GuiConfigurableSlots<TileAdvancedCrafter, ContainerAdvancedCrafter, TileAdvancedCrafter.AUTO_SLOTS> {

    GuiComponentToolTip rfTip;
    GuiComponentArrowProgress progress;
    GuiComponentChangeIconButton buttonMode;

    public GuiAdvancedCrafter(ContainerAdvancedCrafter container) {
        super(container, 176, 166, "tile.modernalchemy.blockAdvancedCrafter.name");
    }

    @Override
    protected Iterable<TileAdvancedCrafter.AUTO_SLOTS> getSlots() {
        return ImmutableList.of(TileAdvancedCrafter.AUTO_SLOTS.output);
    }

    @Override
    public void preRender(float mouseX, float mouseY) {
        super.preRender(mouseX, mouseY);
        TileAdvancedCrafter te = getContainer().getOwner();

        rfTip.setToolTip(te.getEnergyToolTip());
        progress.setMaxProgress(te.requiredProcessTime.get());
        progress.setProgress(te.getProgress().getValue());

        switch (te.currentMode.get()) {
            case TileAdvancedCrafter.ENRICH:
                buttonMode.icon = FakeIcon.createSheetIcon(0, 0, 16, 16);
                break;
            case TileAdvancedCrafter.EXTRUDE:
                buttonMode.icon = FakeIcon.createSheetIcon(16, 0, 32, 16);
                break;
            case TileAdvancedCrafter.BEND:
                buttonMode.icon = FakeIcon.createSheetIcon(32, 0, 48, 16);
                break;
            case TileAdvancedCrafter.FURNACE:
                buttonMode.icon = FakeIcon.createSheetIcon(48, 0, 64, 16);
                break;
        }
    }

    @Override
    protected void addCustomizations(BaseComposite root) {
        final TileAdvancedCrafter te = getContainer().getOwner();

        root.addComponent(progress = new GuiComponentArrowProgress(113, 33, 0));
        root.addComponent(rfTip = new GuiComponentToolTip(15, 20, 20, 50));

        GuiComponentRF energyLevel = new GuiComponentRF(15, 20, 20, 50);
        addSyncUpdateListener(ValueCopyAction.create(te.getRFEnergyStorageProvider(), energyLevel.rfBankReciever()));
        root.addComponent(energyLevel);

        IIcon icon = FakeIcon.createSheetIcon(0, 0, 16, 16);
        ResourceLocation resource = new ResourceLocation(Constants.MODID + ":textures/gui/AC_states.png");

        final ILevelChanger rpc = te.createClientRpcProxy(ILevelChanger.class);
        buttonMode = new GuiComponentChangeIconButton(40, 35, 0xFFFFFF, icon, resource);
        buttonMode.setListener(new IMouseDownListener() {
            @Override
            public void componentMouseDown(BaseComponent component, int x, int y, int button) {
                if (te.currentMode.get() == 3)
                    rpc.changeLevel(0);
                else
                    rpc.changeLevel(te.currentMode.get() + 1);
            }
        });
        root.addComponent(buttonMode);
    }

    @Override
    protected GuiComponentTab createTab(TileAdvancedCrafter.AUTO_SLOTS slot) {
        switch(slot) {
            case output:
                return new GuiComponentTab(StandardPalette.green.getColor(), new ItemStack(ItemHandler.itemReplicationMedium), 100, 100);
            default:
                throw MiscUtils.unhandledEnum(slot);
        }

    }

    @Override
    protected GuiComponentLabel createLabel(TileAdvancedCrafter.AUTO_SLOTS slot) {
        switch(slot) {
            case output:
                return new GuiComponentLabel(22, 82, "Item Control");
            default:
                throw MiscUtils.unhandledEnum(slot);
        }
    }
}
