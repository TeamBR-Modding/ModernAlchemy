package com.dyonovan.modernalchemy.energy;

import cofh.api.energy.EnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import openmods.api.IValueProvider;
import openmods.sync.SyncableObjectBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SyncableRF extends SyncableObjectBase implements IValueProvider<EnergyStorage> {

    private EnergyStorage rfTank;

    public SyncableRF(EnergyStorage energy) {
        rfTank = energy;
    }

    @Override
    public void readFromStream(DataInputStream stream) throws IOException {
        rfTank.setEnergyStored(stream.readInt());
    }

    @Override
    public void writeToStream(DataOutputStream stream) throws IOException {
        stream.writeInt(rfTank.getEnergyStored());
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, String name) {
        nbt.setInteger("Energy", rfTank.getEnergyStored() < 0 ? 0 : rfTank.getEnergyStored());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt, String name) {
        rfTank.setEnergyStored(nbt.getInteger("Energy"));
    }

    @Override
    public EnergyStorage getValue() {
        return rfTank;
    }
}
