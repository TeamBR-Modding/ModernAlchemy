package com.dyonovan.modernalchemy.tileentity.teslacoil;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.tileentity.BaseTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class TileTeslaStand extends BaseTile implements IEnergyHandler {

    protected EnergyStorage energy = new EnergyStorage(1000, 1000, 1000);

    @Override
    public void onWrench(EntityPlayer player) {
        if(getTileInDirection(ForgeDirection.UP) instanceof TileTeslaStand)
            ((TileTeslaStand)getTileInDirection(ForgeDirection.UP)).onWrench(player);
        else if(getTileInDirection(ForgeDirection.UP) instanceof TileTeslaCoil)
            ((TileTeslaCoil)getTileInDirection(ForgeDirection.UP)).onWrench(player);
    }

    @Override
    public void returnWailaHead(List<String> tip) {
        int y = yCoord + 1;
        while(!worldObj.isAirBlock(xCoord, y, zCoord)) {
            if(worldObj.getBlock(xCoord, y, zCoord) == BlockHandler.blockCoil) {
                ((TileTeslaCoil) worldObj.getTileEntity(xCoord, y, zCoord)).returnWailaHead(tip);
                return;
            }
            else if(worldObj.getBlock(xCoord, y, zCoord) != BlockHandler.blockTeslaStand)
                return;
            y++;
        }
    }


    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        energy.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        energy.writeToNBT(tag);
    }

    @Override
    public int receiveEnergy(ForgeDirection side, int maxReceive, boolean simulate) {
        return energy.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection side, int maxReceive, boolean simulate) {
        return energy.extractEnergy(maxReceive, simulate);
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
    public boolean canConnectEnergy(ForgeDirection side) {
        return false;
    }

    @Override
    public void updateEntity() {
        if ((energy.getEnergyStored() > 0)) {
            TileEntity tile = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
            if (tile instanceof TileTeslaStand || tile instanceof TileTeslaCoil) {
                energy.extractEnergy(((IEnergyHandler) tile).receiveEnergy(ForgeDirection.DOWN, energy.extractEnergy(energy.getMaxExtract(), true), false), false);
            }
        }
    }
}
