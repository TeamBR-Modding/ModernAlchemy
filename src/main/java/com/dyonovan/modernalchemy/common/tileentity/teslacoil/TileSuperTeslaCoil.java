package com.dyonovan.modernalchemy.common.tileentity.teslacoil;

import cofh.api.energy.EnergyStorage;
import com.dyonovan.modernalchemy.energy.ITeslaProvider;
import com.dyonovan.modernalchemy.energy.SyncableRF;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import openmods.api.IValueProvider;
import openmods.sync.SyncableBoolean;

import java.util.ArrayList;
import java.util.List;

public class TileSuperTeslaCoil extends TileTeslaCoil implements ITeslaProvider {

    @Override
    protected void createSyncedFields() {
        energyRF = new SyncableRF(new EnergyStorage(10000*8, 1000*8, 0));
        energyTank = new TeslaBank(1000*8);
        isActive = new SyncableBoolean();
    }

    @Override
    public int getEnergyProvided() {
        return ConfigHandler.maxCoilTransfer * 8;
    }

    @Override
    protected boolean doConvert() {
        if (energyRF.getValue().getEnergyStored() > 0 && energyTank.getEnergyLevel()  < energyTank.getMaxCapacity()) {
            isActive.set(true);
            int actualRF = Math.min((ConfigHandler.maxCoilGenerate*8) * (ConfigHandler.rfPerTesla*8), energyRF.getValue().getEnergyStored()*8);
            int actualTesla = Math.min(ConfigHandler.maxCoilGenerate*8, (energyTank.getMaxCapacity() - energyTank.getEnergyLevel())*8);

            if (actualTesla * ConfigHandler.rfPerTesla < actualRF) {
                removeEnergy(actualTesla * 10);
                energyTank.addEnergy(actualTesla);
            } else if (actualTesla * ConfigHandler.rfPerTesla > actualRF && actualRF > 100) {
                removeEnergy(actualRF);
                energyTank.addEnergy(actualRF / ConfigHandler.rfPerTesla);
            } else if (actualTesla * ConfigHandler.rfPerTesla == actualRF) {
                removeEnergy(actualRF);
                energyTank.addEnergy(actualTesla);
            }
            return true;
        }
        isActive.set(false);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return false;
    }

    @Override
    public IValueProvider<TeslaBank> getTeslaBankProvider() {
        return this.energyTank;
    }

    @Override
    public IValueProvider<EnergyStorage> getRFEnergyStorageProvider() {
        return this.energyRF;
    }

    @Override
    public List<String> getTeslaEnergyToolTip() {
        List<String> toolTip = new ArrayList<>();
        toolTip.add(GuiHelper.GuiColor.WHITE + "Energy Stored");
        toolTip.add("" + GuiHelper.GuiColor.YELLOW + energyTank.getValue().getEnergyLevel() + "/" + energyTank.getValue().getMaxCapacity() + GuiHelper.GuiColor.BLUE + "T");
        return toolTip;
    }

    @Override
    public List<String> getEnergyToolTip() {
        List<String> toolTip = new ArrayList<>();
        toolTip.add(GuiHelper.GuiColor.WHITE + "RF Energy");
        toolTip.add("" + GuiHelper.GuiColor.YELLOW + energyRF.getValue().getEnergyStored() + "/" + energyRF.getValue().getMaxEnergyStored() + GuiHelper.GuiColor.RED + "RF");
        return toolTip;
    }
}
