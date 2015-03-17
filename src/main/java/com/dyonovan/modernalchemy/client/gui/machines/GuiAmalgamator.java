package com.dyonovan.modernalchemy.client.gui.machines;

import com.dyonovan.modernalchemy.client.gui.GuiBaseConfigurableSlots;
import com.dyonovan.modernalchemy.client.gui.INeiProvider;
import com.dyonovan.modernalchemy.client.gui.StandardPalette;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentReverseTab;
import com.dyonovan.modernalchemy.client.rpc.IRedstoneRequired;
import com.dyonovan.modernalchemy.common.container.machines.ContainerAmalgamator;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentArrowProgress;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentTeslaBank;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentToolTip;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.common.tileentity.machines.TileAmalgamator;
import com.google.common.collect.ImmutableList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import openmods.gui.component.*;
import openmods.gui.listener.IMouseDownListener;
import openmods.gui.logic.ValueCopyAction;
import openmods.utils.MiscUtils;

import java.util.ArrayList;
import java.util.List;

public class GuiAmalgamator extends GuiBaseConfigurableSlots<TileAmalgamator, ContainerAmalgamator, TileAmalgamator.AUTO_SLOTS> implements INeiProvider{

    GuiComponentToolTip tankTip;
    GuiComponentToolTip energyTip;

    public GuiAmalgamator(ContainerAmalgamator container) {
        super(container, 176, 166, "tile.modernalchemy.blockAmalgamator.name");
        setNEIArrowLocation(100, 37, 24, 15);
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
        switch(slot) {
            case liquid :
                GuiComponentTab liquidTab = new GuiComponentTab(StandardPalette.lightblue.getColor(), new ItemStack(ItemHandler.itemBucketActinium), 100, 100);
                return liquidTab;
            case output :
                GuiComponentTab outputTab = new GuiComponentTab(StandardPalette.green.getColor(), new ItemStack(ItemHandler.itemReplicationMedium), 100, 100);
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

    @Override
    public String getNeiLabel() {
        return "modernalchemy.solidifier.recipes";
    }
}