package com.dyonovan.itemreplication.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.dyonovan.itemreplication.energy.ITeslaHandler;
import com.dyonovan.itemreplication.energy.TeslaBank;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileTeslaCoil extends BaseTile implements IEnergyHandler, ITeslaHandler {

    protected EnergyStorage energy = new EnergyStorage(10000, 1000, 0);

    public TileTeslaCoil() {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        energy.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        energy.writeToNBT(tag);
    }

    @Override
    public int receiveEnergy(ForgeDirection side, int maxReceive, boolean simulate) {
        return energy.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int i, boolean b) {
        return 0;
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return energy.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return energy.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection forgeDirection) {
        return false;
    }

    // Tesla
    @Override
    public void addEnergy(int maxAmount) {

    }

    // Tesla
    @Override
    public void drainEnergy(int maxAmount) {

    }

    // Tesla
    @Override
    public int getEnergyLevel() {
        return 0;
    }

    // Tesla
    @Override
    public TeslaBank getEnergyBank() {
        return null;
    }
}
