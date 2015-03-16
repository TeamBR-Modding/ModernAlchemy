package com.dyonovan.modernalchemy.client.gui.machines;

import com.dyonovan.modernalchemy.client.gui.GuiBaseConfigurableSlots;
import com.dyonovan.modernalchemy.client.gui.StandardPalette;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentTeslaBank;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentToolTip;
import com.dyonovan.modernalchemy.common.container.machines.ContainerElectricBellows;
import com.dyonovan.modernalchemy.common.tileentity.machines.TileElectricBellows;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import openmods.gui.component.BaseComposite;
import openmods.gui.component.GuiComponentLabel;
import openmods.gui.component.GuiComponentTab;
import openmods.gui.component.GuiComponentTankLevel;
import openmods.gui.logic.ValueCopyAction;
import openmods.utils.MiscUtils;

import java.util.ArrayList;
import java.util.List;

public class GuiElectricBellows extends GuiBaseConfigurableSlots<TileElectricBellows, ContainerElectricBellows, TileElectricBellows.AutoSlots> {

    GuiComponentToolTip tankTip;
    GuiComponentToolTip energyTip;

    public GuiElectricBellows(ContainerElectricBellows container) {
        super(container, 150, 120, "tile.modernalchemy.blockElectricBellows.name");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String machineName = StatCollector.translateToLocal(name);
        int x = this.xSize / 2 - (fontRendererObj.getStringWidth(machineName) / 2);
        fontRendererObj.drawString(machineName, x, 6, 4210752);
    }

    @Override
    protected Iterable<TileElectricBellows.AutoSlots> getSlots() {
        return ImmutableList.of(TileElectricBellows.AutoSlots.fluid);
    }

    @Override
    public void preRender(float mouseX, float mouseY) {
        super.preRender(mouseX, mouseY);
        TileElectricBellows tileAmalgamator = getContainer().getOwner();

        tankTip.setToolTip(tileAmalgamator.getFluidToolTip());
        energyTip.setToolTip(tileAmalgamator.getEnergyToolTip());
    }

    @Override
    protected void addCustomizations(BaseComposite root) {
        TileElectricBellows tile = getContainer().getOwner();

        GuiComponentTankLevel tank = new GuiComponentTankLevel(35, 20, 105, 90, TileElectricBellows.TANK_CAPACITY);
        addSyncUpdateListener(ValueCopyAction.create(tile.getFluidProvider(), tank.fluidReceiver()));
        root.addComponent(tank);

        GuiComponentTeslaBank energy = new GuiComponentTeslaBank(10, 20, 15, 90);
        addSyncUpdateListener(ValueCopyAction.create(tile.getTeslaBankProvider(), energy.teslaBankReciever()));
        root.addComponent(energy);

        root.addComponent(energyTip = new GuiComponentToolTip(10, 20, 15, 90));
        root.addComponent(tankTip = new GuiComponentToolTip(35, 20, 105, 90));
    }

    @Override
    protected GuiComponentTab createTab(TileElectricBellows.AutoSlots slot) {
        switch (slot) {
            case fluid:
                return new GuiComponentTab(StandardPalette.blue.getColor(), new ItemStack(BlockHandler.blockElectricBellows), 100, 100);
            default :
                throw MiscUtils.unhandledEnum(slot);
        }
    }

    @Override
    protected GuiComponentLabel createLabel(TileElectricBellows.AutoSlots slot) {
        switch (slot) {
            case fluid :
                return new GuiComponentLabel(22, 82, "Auto-Export");
            default :
                throw MiscUtils.unhandledEnum(slot);
        }
    }

    @Override
    public List<GuiComponentTab> getExtraTabs() {
        return new ArrayList<>();
    }
}
