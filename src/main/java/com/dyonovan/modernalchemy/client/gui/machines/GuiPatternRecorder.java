package com.dyonovan.modernalchemy.client.gui.machines;

import com.dyonovan.modernalchemy.client.gui.GuiBaseConfigurableSlots;
import com.dyonovan.modernalchemy.client.gui.INeiProvider;
import com.dyonovan.modernalchemy.client.gui.StandardPalette;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentArrowProgress;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentTeslaBank;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentToolTip;
import com.dyonovan.modernalchemy.common.container.machines.ContainerPatternRecorder;
import com.dyonovan.modernalchemy.common.tileentity.machines.TilePatternRecorder;
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

public class GuiPatternRecorder extends GuiBaseConfigurableSlots<TilePatternRecorder, ContainerPatternRecorder, TilePatternRecorder.AUTO_SLOTS> {

    GuiComponentToolTip energyTip;

    public GuiPatternRecorder(ContainerPatternRecorder container){
        super(container, 176, 166, "tile.modernalchemy.blockPatternRecorder.name");
    }

    @Override
    protected Iterable<TilePatternRecorder.AUTO_SLOTS> getSlots() {
        return ImmutableList.of(TilePatternRecorder.AUTO_SLOTS.output, TilePatternRecorder.AUTO_SLOTS.input);
    }

    @Override
    public void preRender(float mouseX, float mouseY) {
        super.preRender(mouseX, mouseY);
        TilePatternRecorder te = getContainer().getOwner();

        energyTip.setToolTip(te.getEnergyToolTip());
    }

    @Override
    protected void addCustomizations(BaseComposite root) {
        TilePatternRecorder te = getContainer().getOwner();

        GuiComponentTeslaBank energyLevel = new GuiComponentTeslaBank(15, 20, 20, 50);
        addSyncUpdateListener(ValueCopyAction.create(te.getTeslaBankProvider(), energyLevel.teslaBankReciever()));
        root.addComponent(energyLevel);
        root.addComponent(energyTip = new GuiComponentToolTip(15, 20, 20, 50));

        GuiComponentArrowProgress progress = new GuiComponentArrowProgress(110, 34, TilePatternRecorder.PROCESS_TIME);
        addSyncUpdateListener(ValueCopyAction.create(te.getProgress(), progress.progressReceiver()));
        root.addComponent(progress);
    }

    @Override
    protected GuiComponentTab createTab(TilePatternRecorder.AUTO_SLOTS slot) {
        switch (slot) {
            case output :
                return new GuiComponentTab(StandardPalette.green.getColor(), new ItemStack(BlockHandler.blockPatternRecorder), 100, 100);
            case input:
                return new GuiComponentTab(StandardPalette.blue.getColor(), new ItemStack(ItemHandler.itemReplicatorPattern), 100, 100);
            default :
                throw MiscUtils.unhandledEnum(slot);
        }
    }

    @Override
    protected GuiComponentLabel createLabel(TilePatternRecorder.AUTO_SLOTS slot) {
        switch (slot) {
            case output :
                return new GuiComponentLabel(22, 82, "Auto-Export");
            case input :
                return new GuiComponentLabel(22, 82, "Auto-Import");
            default :
                throw MiscUtils.unhandledEnum(slot);
        }
    }

    @Override
    public List<GuiComponentTab> getExtraTabs() {
        return new ArrayList<>();
    }
}
