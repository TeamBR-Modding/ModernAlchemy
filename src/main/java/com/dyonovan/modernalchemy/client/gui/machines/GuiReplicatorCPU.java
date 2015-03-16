package com.dyonovan.modernalchemy.client.gui.machines;

import com.dyonovan.modernalchemy.client.gui.GuiBaseConfigurableSlots;
import com.dyonovan.modernalchemy.client.gui.StandardPalette;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentArrowProgress;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentTeslaBank;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentToolTip;
import com.dyonovan.modernalchemy.common.container.machines.ContainerReplicatorCpu;
import com.dyonovan.modernalchemy.common.tileentity.machines.TilePatternRecorder;
import com.dyonovan.modernalchemy.common.tileentity.replicator.TileReplicatorCPU;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import openmods.gui.component.BaseComposite;
import openmods.gui.component.GuiComponentLabel;
import openmods.gui.component.GuiComponentTab;
import openmods.gui.logic.ValueCopyAction;
import openmods.utils.MiscUtils;

import java.util.ArrayList;
import java.util.List;

public class GuiReplicatorCPU extends GuiBaseConfigurableSlots<TileReplicatorCPU, ContainerReplicatorCpu, TileReplicatorCPU.AUTO_SLOTS> {

    GuiComponentToolTip energyTip;
    GuiComponentArrowProgress progress;

    public GuiReplicatorCPU(ContainerReplicatorCpu container) {
        super(container, 176, 166, "tile.modernalchemy.blockReplicatorCPU.name");
    }

    @Override
    protected Iterable<TileReplicatorCPU.AUTO_SLOTS> getSlots() {
        return ImmutableList.of(TileReplicatorCPU.AUTO_SLOTS.medium_input, TileReplicatorCPU.AUTO_SLOTS.output);
    }

    @Override
    public void preRender(float mouseX, float mouseY) {
        super.preRender(mouseX, mouseY);
        TileReplicatorCPU tileReplicatorCPU = getContainer().getOwner();

        energyTip.setToolTip(tileReplicatorCPU.getEnergyToolTip());
        progress.setMaxProgress(tileReplicatorCPU.requiredProcessTime.get());
        progress.setProgress(tileReplicatorCPU.getProgress().getValue());
    }

    @Override
    protected void addCustomizations(BaseComposite root) {
        TileReplicatorCPU tileReplicatorCPU = getContainer().getOwner();

        GuiComponentTeslaBank energyLevel = new GuiComponentTeslaBank(15, 20, 20, 50);
        addSyncUpdateListener(ValueCopyAction.create(tileReplicatorCPU.getTeslaBankProvider(), energyLevel.teslaBankReciever()));
        root.addComponent(energyLevel);

        root.addComponent(energyTip = new GuiComponentToolTip(15, 20, 20, 50));

        root.addComponent(progress = new GuiComponentArrowProgress(110, 34, 0));
    }

    @Override
    protected GuiComponentTab createTab(TileReplicatorCPU.AUTO_SLOTS slot) {
        switch (slot) {
            case medium_input:
                return new GuiComponentTab(StandardPalette.green.getColor(), new ItemStack(ItemHandler.itemReplicationMedium), 100, 100);
            case output :
                return new GuiComponentTab(StandardPalette.green.getColor(), new ItemStack(BlockHandler.blockReplicatorCPU), 100, 100);
            default :
                throw MiscUtils.unhandledEnum(slot);

        }
    }

    @Override
    protected GuiComponentLabel createLabel(TileReplicatorCPU.AUTO_SLOTS slot) {
        switch(slot) {
            case medium_input:
                return new GuiComponentLabel(22, 82, "Auto-Import");
            case output:
                return new GuiComponentLabel(22, 82, "Auto-Export");
            default:
                throw MiscUtils.unhandledEnum(slot);
        }
    }

    @Override
    public List<GuiComponentTab> getExtraTabs() {
        return new ArrayList<>();
    }
}
