package com.dyonovan.modernalchemy.tileentity.teslacoil;

import cofh.api.energy.EnergyStorage;
import com.dyonovan.modernalchemy.energy.ITeslaProvider;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;

public class TileSuperTeslaCoil extends TileTeslaCoil implements ITeslaProvider {

    public TileSuperTeslaCoil() {
        super();
        energyRF = new EnergyStorage(10000*8, 1000*8, 0);
        energyTank = new TeslaBank(1000*8);
    }

    @Override
    public int getEnergyProvided() {
        return ConfigHandler.maxCoilTransfer * 8;
    }

    @Override
    protected boolean doConvert() {
        if (energyRF.getEnergyStored() > 0 && energyTank.getEnergyLevel()  < energyTank.getMaxCapacity()) {
            isActive = true;
            int actualRF = Math.min((ConfigHandler.maxCoilGenerate*8) * (ConfigHandler.rfPerTesla*8), energyRF.getEnergyStored()*8);
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
        isActive = false;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return false;
    }
}
