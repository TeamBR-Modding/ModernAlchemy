package com.dyonovan.modernalchemy.gui;

import com.dyonovan.modernalchemy.container.ContainerArcFurnace;
import com.dyonovan.modernalchemy.gui.components.GuiComponentArrowProgress;
import com.dyonovan.modernalchemy.gui.components.GuiComponentTeslaBank;
import com.dyonovan.modernalchemy.gui.components.GuiComponentToolTip;
import com.dyonovan.modernalchemy.gui.widgets.WidgetEnergyBank;
import com.dyonovan.modernalchemy.gui.widgets.WidgetPulse;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.TileArcFurnaceCore;
import com.dyonovan.teambrcore.gui.BaseGui;
import com.dyonovan.teambrcore.gui.widget.WidgetLiquidTank;
import com.dyonovan.teambrcore.helpers.GuiHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import openmods.gui.SyncedGuiContainer;
import openmods.gui.component.BaseComposite;
import openmods.gui.component.GuiComponentTankLevel;
import openmods.gui.logic.ValueCopyAction;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

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
