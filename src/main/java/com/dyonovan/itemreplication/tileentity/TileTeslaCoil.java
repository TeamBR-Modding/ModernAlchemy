package com.dyonovan.itemreplication.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.dyonovan.itemreplication.energy.ITeslaHandler;
import com.dyonovan.itemreplication.energy.TeslaBank;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileTeslaCoil extends BaseTile implements IEnergyHandler, ITeslaHandler {

    protected EnergyStorage energyRF = new EnergyStorage(10000, 1000, 0);
    private TeslaBank energyTesla = new TeslaBank(0, 1000);

    public TileTeslaCoil() {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        energyRF.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        energyRF.writeToNBT(tag);
    }

    @Override
    public int receiveEnergy(ForgeDirection side, int maxReceive, boolean simulate) {
        return energyRF.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int i, boolean b) {
        return 0;
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return energyRF.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return energyRF.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection forgeDirection) {
        return false;
    }

    @Override
    public void addEnergy(int maxAmount) {
        energyTesla.addEnergy(maxAmount);
    }

    @Override
    public void drainEnergy(int maxAmount) {
        energyTesla.drainEnergy(maxAmount);
    }

    @Override
    public int getEnergyLevel() {
        return energyTesla.getEnergyLevel();
    }

    @Override
    public TeslaBank getEnergyBank() {
        return energyTesla;
    }
}
