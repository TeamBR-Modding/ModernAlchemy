package com.dyonovan.modernalchemy.client.gui.machines;

import com.dyonovan.modernalchemy.client.gui.GuiBaseConfigurableSlots;
import com.dyonovan.modernalchemy.client.gui.StandardPalette;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentArrowProgress;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentReverseTab;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentTeslaBank;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentToolTip;
import com.dyonovan.modernalchemy.client.rpc.IRedstoneRequired;
import com.dyonovan.modernalchemy.common.container.machines.ContainerReplicatorCpu;
import com.dyonovan.modernalchemy.common.tileentity.machines.TileReplicator;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.google.common.collect.ImmutableList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import openmods.gui.component.*;
import openmods.gui.listener.IMouseDownListener;
import openmods.gui.logic.ValueCopyAction;
import openmods.utils.MiscUtils;

import java.util.ArrayList;
import java.util.List;

public class GuiReplicator extends GuiBaseConfigurableSlots<TileReplicator, ContainerReplicatorCpu, TileReplicator.AUTO_SLOTS> {

    GuiComponentToolTip energyTip;
    GuiComponentArrowProgress progress;

    public GuiReplicator(ContainerReplicatorCpu container) {
        super(container, 176, 166, "tile.modernalchemy.blockReplicator.name");
    }

    @Override
    protected Iterable<TileReplicator.AUTO_SLOTS> getSlots() {
        return ImmutableList.of(TileReplicator.AUTO_SLOTS.medium_input, TileReplicator.AUTO_SLOTS.output);
    }

    @Override
    public void preRender(float mouseX, float mouseY) {
        super.preRender(mouseX, mouseY);
        TileReplicator tileReplicator = getContainer().getOwner();

        energyTip.setToolTip(tileReplicator.getEnergyToolTip());
        progress.setMaxProgress(tileReplicator.requiredProcessTime.get());
        progress.setProgress(tileReplicator.getProgress().getValue());
    }

    @Override
    protected void addCustomizations(BaseComposite root) {
        TileReplicator tileReplicator = getContainer().getOwner();

        GuiComponentTeslaBank energyLevel = new GuiComponentTeslaBank(15, 20, 20, 50);
        addSyncUpdateListener(ValueCopyAction.create(tileReplicator.getTeslaBankProvider(), energyLevel.teslaBankReciever()));
        root.addComponent(energyLevel);

        root.addComponent(energyTip = new GuiComponentToolTip(15, 20, 20, 50));

        root.addComponent(progress = new GuiComponentArrowProgress(110, 34, 0));
    }

    @Override
    protected GuiComponentTab createTab(TileReplicator.AUTO_SLOTS slot) {
        switch (slot) {
            case medium_input:
                return new GuiComponentTab(StandardPalette.green.getColor(), new ItemStack(ItemHandler.itemReplicationMedium), 100, 100);
            case output :
                return new GuiComponentTab(StandardPalette.green.getColor(), new ItemStack(BlockHandler.blockReplicator), 100, 100);
            default :
                throw MiscUtils.unhandledEnum(slot);

        }
    }

    @Override
    protected GuiComponentLabel createLabel(TileReplicator.AUTO_SLOTS slot) {
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
        List<GuiComponentTab> tabs = new ArrayList<>();

        GuiComponentReverseTab redstoneTab = new GuiComponentReverseTab(StandardPalette.red.getColor(), new ItemStack(Items.redstone), 100, 50, xSize);
        redstoneTab.addComponent(new GuiComponentLabel(30, 10,"Redstone"));
        final GuiComponentCheckbox requiredRedstone = new GuiComponentCheckbox(10, 25, false, 0xFFFFFF);
        requiredRedstone.setValue(getContainer().getOwner().getRedstoneRequiredProvider().getValue());
        final IRedstoneRequired rpc = getContainer().getOwner().createClientRpcProxy(IRedstoneRequired.class);
        requiredRedstone.setListener(new IMouseDownListener() {
            @Override
            public void componentMouseDown(BaseComponent component, int x, int y, int button) {
                if(getContainer().getOwner().getRedstoneRequiredProvider().getValue()) {
                    rpc.setRequirement(false);
                }
                else {
                    rpc.setRequirement(true);
                }
                requiredRedstone.setValue(getContainer().getOwner().getRedstoneRequiredProvider().getValue());
            }
        });
        redstoneTab.addComponent(requiredRedstone);
        redstoneTab.addComponent(new GuiComponentLabel(20, 25, "Required"));
        tabs.add(redstoneTab);

        return tabs;
    }
}
