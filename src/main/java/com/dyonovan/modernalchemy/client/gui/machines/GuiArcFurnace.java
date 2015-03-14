package com.dyonovan.modernalchemy.client.gui.machines;

import com.dyonovan.modernalchemy.common.container.machines.ContainerArcFurnace;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentArrowProgress;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentTeslaBank;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentToolTip;
import com.dyonovan.modernalchemy.common.tileentity.arcfurnace.TileArcFurnaceCore;
import openmods.gui.SyncedGuiContainer;
import openmods.gui.component.BaseComposite;
import openmods.gui.component.GuiComponentTankLevel;
import openmods.gui.logic.ValueCopyAction;

public class GuiArcFurnace extends SyncedGuiContainer<ContainerArcFurnace> {

    GuiComponentToolTip energyTip;
    GuiComponentToolTip airTankTip;
    GuiComponentToolTip outputTankTip;

    public GuiArcFurnace(ContainerArcFurnace container) {
        super(container, 176, 166, "tile.modernalchemy.blockArcFurnaceCore.name");
    }

    @Override
    public void preRender(float mouseX, float mouseY) {
        super.preRender(mouseX, mouseY);
        TileArcFurnaceCore te = getContainer().getOwner();

        energyTip.setToolTip(te.getEnergyToolTip());
        airTankTip.setToolTip(te.getAirTankToolTip());
        outputTankTip.setToolTip(te.getOutputTankToolTip());
    }

    @Override
    protected BaseComposite createRoot() {
        TileArcFurnaceCore arcFurnaceCore = getContainer().getOwner();
        BaseComposite main = super.createRoot();

        GuiComponentTeslaBank energyTank = new GuiComponentTeslaBank(8, 20, 20, 50);
        addSyncUpdateListener(ValueCopyAction.create(arcFurnaceCore.getEnergyProvider(), energyTank.teslaBankReciever()));
        main.addComponent(energyTank);

        GuiComponentTankLevel airTank = new GuiComponentTankLevel(34, 20, 30, 50, TileArcFurnaceCore.TANK_CAPACITY);
        addSyncUpdateListener(ValueCopyAction.create(arcFurnaceCore.getAirTankProvider(), airTank.fluidReceiver()));
        main.addComponent(airTank);

        GuiComponentTankLevel outputTank = new GuiComponentTankLevel(135, 20, 35, 50, TileArcFurnaceCore.TANK_CAPACITY);
        addSyncUpdateListener(ValueCopyAction.create(arcFurnaceCore.getOutputTankProvider(), outputTank.fluidReceiver()));
        main.addComponent(outputTank);

        GuiComponentArrowProgress arrow = new GuiComponentArrowProgress(105, 36, TileArcFurnaceCore.COOK_TIME);
        addSyncUpdateListener(ValueCopyAction.create(arcFurnaceCore.getProgress(), arrow.progressReceiver()));
        main.addComponent(arrow);

        main.addComponent(airTankTip = new GuiComponentToolTip(34, 20, 30, 50));
        main.addComponent(outputTankTip = new GuiComponentToolTip(135, 20, 35, 50));
        main.addComponent(energyTip = new GuiComponentToolTip(8, 20, 20, 50));

        return main;
    }
}
