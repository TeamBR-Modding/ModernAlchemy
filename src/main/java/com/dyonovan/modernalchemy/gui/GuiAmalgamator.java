package com.dyonovan.modernalchemy.gui;

import com.dyonovan.modernalchemy.container.ContainerAmalgamator;
import com.dyonovan.modernalchemy.gui.components.GuiComponentArrowProgress;
import com.dyonovan.modernalchemy.gui.components.GuiComponentTeslaBank;
import com.dyonovan.modernalchemy.gui.components.GuiComponentToolTip;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.tileentity.machines.TileAmalgamator;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import openmods.gui.GuiConfigurableSlots;
import openmods.gui.component.*;
import openmods.gui.logic.ValueCopyAction;
import openmods.utils.MiscUtils;
import scala.actors.threadpool.Arrays;

public class GuiAmalgamator extends GuiConfigurableSlots<TileAmalgamator, ContainerAmalgamator, TileAmalgamator.AUTO_SLOTS> {

    public GuiAmalgamator(ContainerAmalgamator container) {
        super(container, 176, 166, "tile.modernalchemy.blockAmalgamator.name");
    }

    @Override
    protected Iterable<TileAmalgamator.AUTO_SLOTS> getSlots() {
        return ImmutableList.of(TileAmalgamator.AUTO_SLOTS.liquid, TileAmalgamator.AUTO_SLOTS.output);
    }

    @Override
    protected void addCustomizations(BaseComposite root) {
        TileAmalgamator te = getContainer().getOwner();

        GuiComponentTankLevel tankLevel = new GuiComponentTankLevel(40, 20, 30, 50, TileAmalgamator.TANK_CAPACITY);
        addSyncUpdateListener(ValueCopyAction.create(te.getFluidProvider(), tankLevel.fluidReceiver()));
        root.addComponent(tankLevel);

        GuiComponentToolTip tankTip = new GuiComponentToolTip(40, 20, 30, 50);
        addSyncUpdateListener(ValueCopyAction.create(te.getFluidTooltip(), tankTip.toolTipProvider()));
        root.addComponent(tankTip);

        GuiComponentTeslaBank energyLevel = new GuiComponentTeslaBank(15, 20, 20, 50);
        addSyncUpdateListener(ValueCopyAction.create(te.getTeslaBankProvider(), energyLevel.teslaBankReciever()));
        root.addComponent(energyLevel);

        GuiComponentToolTip energyTip = new GuiComponentToolTip(15, 20, 20, 50);
        addSyncUpdateListener(ValueCopyAction.create(te.getEnergyToolTip(), energyTip.toolTipProvider()));
        root.addComponent(energyTip);

        GuiComponentArrowProgress progress = new GuiComponentArrowProgress(100, 37, TileAmalgamator.PROCESS_TIME);
        addSyncUpdateListener(ValueCopyAction.create(te.getProgress(), progress.progressReceiver()));
        root.addComponent(progress);
    }

    @Override
    protected GuiComponentTab createTab(TileAmalgamator.AUTO_SLOTS slot) {
        switch(slot) {
            case liquid :
                return new GuiComponentTab(StandardPalette.lightblue.getColor(), new ItemStack(ItemHandler.itemBucketActinium), 100, 100);
            case output :
                return new GuiComponentTab(StandardPalette.green.getColor(), new ItemStack(ItemHandler.itemReplicationMedium), 100, 100);
            default :
                throw MiscUtils.unhandledEnum(slot);
        }
    }

    @Override
    protected GuiComponentLabel createLabel(TileAmalgamator.AUTO_SLOTS slot) {
        switch(slot) {
            case liquid :
                return new GuiComponentLabel(22, 82, "Auto-Import");
            case output :
                return new GuiComponentLabel(22, 82, "Auto-Export");
            default :
                throw MiscUtils.unhandledEnum(slot);
        }
    }
}