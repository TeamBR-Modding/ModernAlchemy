package com.dyonovan.modernalchemy.tileentity.teslacoil;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.tileentity.BaseMachine;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileTeslaCoil extends BaseMachine implements IEnergyHandler {

    protected EnergyStorage energyRF;


    public TileTeslaCoil() {
        super();
        energyRF = new EnergyStorage(10000, 1000, 0);
        energyTank = new TeslaBank(1000);
    }

    /*******************************************************************************************************************
     ************************************* Tesla Coil Functions ********************************************************
     *******************************************************************************************************************/

    private boolean doConvert() {
        if (energyRF.getEnergyStored() > 0 && energyTank.getEnergyLevel()  < energyTank.getMaxCapacity()) {
            int actualRF = Math.min(ConfigHandler.maxCoilGenerate * ConfigHandler.rfPerTesla, energyRF.getEnergyStored());
            int actualTesla = Math.min(ConfigHandler.maxCoilGenerate, energyTank.getMaxCapacity() - energyTank.getEnergyLevel());

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
        return false;
    }

    /*******************************************************************************************************************
     ******************************************** Energy Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public int receiveEnergy(ForgeDirection side, int maxReceive, boolean simulate) {
        return energyRF.receiveEnergy(maxReceive, simulate);
    }

    /*public void setRFEnergyStored(int i) {
        energyRF.setEnergyStored(i);
    }*/

    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int maxReceive, boolean simulate) {
        return energyRF.extractEnergy(maxReceive, simulate);
    }

    public void removeEnergy(int amount) {
        energyRF.setEnergyStored(energyRF.getEnergyStored() - amount);
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return energyRF.getEnergyStored();
    }

    public int getRFEnergyStored() {
        return energyRF.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return energyRF.getMaxEnergyStored();
    }

    public int getRFMaxEnergyStored() {
        return energyRF.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection side) {
        return side == ForgeDirection.DOWN; //TODO Why BC Pipes dont update on load
    }

    /*public void setTeslaEnergyStored(int i) {
        energyTank.setEnergyLevel(i);
    }*/

    /*******************************************************************************************************************
     ********************************************** Tile Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        energyRF.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        energyRF.writeToNBT(tag);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) return;
        if(doConvert())
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}
