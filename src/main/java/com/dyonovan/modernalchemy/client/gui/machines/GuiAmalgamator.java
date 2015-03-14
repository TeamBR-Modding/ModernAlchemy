package com.dyonovan.modernalchemy.client.gui.machines;

import com.dyonovan.modernalchemy.client.gui.GuiBaseConfigurableSlots;
import com.dyonovan.modernalchemy.client.gui.INeiProvider;
import com.dyonovan.modernalchemy.client.gui.StandardPalette;
import com.dyonovan.modernalchemy.common.container.machines.ContainerAmalgamator;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentArrowProgress;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentTeslaBank;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentToolTip;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.common.tileentity.machines.TileAmalgamator;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import openmods.gui.component.*;
import openmods.gui.logic.ValueCopyAction;
import openmods.utils.MiscUtils;

import java.util.ArrayList;

public class GuiAmalgamator extends GuiBaseConfigurableSlots<TileAmalgamator, ContainerAmalgamator, TileAmalgamator.AUTO_SLOTS> implements INeiProvider{

    GuiComponentToolTip tankTip;
    GuiComponentToolTip energyTip;

    public GuiAmalgamator(ContainerAmalgamator container) {
        super(container, 176, 166, "tile.modernalchemy.blockAmalgamator.name");
        setArrowLocation(100, 37, 24, 15);
    }

    @Override
    protected Iterable<TileAmalgamator.AUTO_SLOTS> getSlots() {
        return ImmutableList.of(TileAmalgamator.AUTO_SLOTS.liquid, TileAmalgamator.AUTO_SLOTS.output);
    }

    @Override
    public void preRender(float mouseX, float mouseY) {
        super.preRender(mouseX, mouseY);
        TileAmalgamator tileAmalgamator = getContainer().getOwner();

        tankTip.setToolTip(tileAmalgamator.getFluidToolTip());
        energyTip.setToolTip(tileAmalgamator.getEnergyToolTip());
    }

    @Override
    protected void addCustomizations(BaseComposite root) {
        TileAmalgamator te = getContainer().getOwner();

        GuiComponentTankLevel tankLevel = new GuiComponentTankLevel(40, 20, 30, 50, TileAmalgamator.TANK_CAPACITY);
        addSyncUpdateListener(ValueCopyAction.create(te.getFluidProvider(), tankLevel.fluidReceiver()));
        root.addComponent(tankLevel);

        root.addComponent(tankTip = new GuiComponentToolTip(40, 20, 30, 50));

        GuiComponentTeslaBank energyLevel = new GuiComponentTeslaBank(15, 20, 20, 50);
        addSyncUpdateListener(ValueCopyAction.create(te.getTeslaBankProvider(), energyLevel.teslaBankReciever()));
        root.addComponent(energyLevel);

        root.addComponent(energyTip = new GuiComponentToolTip(15, 20, 20, 50));

        GuiComponentArrowProgress progress = new GuiComponentArrowProgress(100, 37, TileAmalgamator.PROCESS_TIME);
        addSyncUpdateListener(ValueCopyAction.create(te.getProgress(), progress.progressReceiver()));
        root.addComponent(progress);
    }

    @Override
    protected GuiComponentTab createTab(TileAmalgamator.AUTO_SLOTS slot) {
        if(tabs == null)
            tabs = new ArrayList<>();

        switch(slot) {
            case liquid :
                GuiComponentTab liquidTab = new GuiComponentTab(StandardPalette.lightblue.getColor(), new ItemStack(ItemHandler.itemBucketActinium), 100, 100);
                tabs.add(liquidTab);
                return liquidTab;
            case output :
                GuiComponentTab outputTab = new GuiComponentTab(StandardPalette.green.getColor(), new ItemStack(ItemHandler.itemReplicationMedium), 100, 100);
                tabs.add(outputTab);
                return outputTab;
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

    @Override
    public String getNeiLabel() {
        return "modernalchemy.solidifier.recipes";
    }
}