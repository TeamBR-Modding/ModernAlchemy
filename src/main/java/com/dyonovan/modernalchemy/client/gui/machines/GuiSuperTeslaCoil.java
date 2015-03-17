package com.dyonovan.modernalchemy.client.gui.machines;

import com.dyonovan.modernalchemy.client.gui.components.GuiComponentRF;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentTeslaBank;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentToolTip;
import com.dyonovan.modernalchemy.client.notification.GuiColor;
import com.dyonovan.modernalchemy.common.container.ContainerSuperTeslaCoil;
import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileSuperTeslaCoil;
import net.minecraft.util.StatCollector;
import openmods.gui.SyncedGuiContainer;
import openmods.gui.component.BaseComposite;
import openmods.gui.component.GuiComponentLabel;
import openmods.gui.logic.ValueCopyAction;

public class GuiSuperTeslaCoil extends SyncedGuiContainer<ContainerSuperTeslaCoil> {

    GuiComponentToolTip teslaTip;
    GuiComponentToolTip rfTip;
    GuiComponentLabel restricted;

    public GuiSuperTeslaCoil(ContainerSuperTeslaCoil container) {
        super(container, 100, 100, "tile.modernalchemy.blockTeslaCoil.name");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String machineName = StatCollector.translateToLocal(name);
        int x = this.xSize / 2 - (fontRendererObj.getStringWidth(machineName) / 2);
        fontRendererObj.drawString(machineName, x, 6, 4210752);
    }

    @Override
    public void preRender(float mouseX, float mouseY) {
        super.preRender(mouseX, mouseY);
        TileSuperTeslaCoil coil = getContainer().getOwner();

        teslaTip.setToolTip(coil.getTeslaEnergyToolTip());
        rfTip.setToolTip(coil.getEnergyToolTip());

        String newLabel = coil.linkedMachines.size() > 0 ? GuiColor.RED + "Restricted" : GuiColor.GREEN + "Any";
        restricted.setX(50 - (fontRendererObj.getStringWidth(newLabel) / 2));
        restricted.setMaxWidth(fontRendererObj.getStringWidth(newLabel));
        restricted.setText(newLabel);
    }

    @Override
    protected BaseComposite createRoot() {
        TileSuperTeslaCoil coil = getContainer().getOwner();
        BaseComposite main = super.createRoot();

        GuiComponentRF rfEnergy = new GuiComponentRF(7, 15, 15, 75);
        addSyncUpdateListener(ValueCopyAction.create(coil.getRFEnergyStorageProvider(), rfEnergy.rfBankReciever()));
        main.addComponent(rfEnergy);

        GuiComponentTeslaBank teslaBank = new GuiComponentTeslaBank(78, 15, 15, 75);
        addSyncUpdateListener(ValueCopyAction.create(coil.getTeslaBankProvider(), teslaBank.teslaBankReciever()));
        main.addComponent(teslaBank);

        main.addComponent(restricted = new GuiComponentLabel(50, 45, "Any"));
        main.addComponent(rfTip = new GuiComponentToolTip(7, 15, 15, 75));
        main.addComponent(teslaTip = new GuiComponentToolTip(78, 15, 15, 75));

        return main;
    }
}
