package com.dyonovan.modernalchemy.common.tileentity.teslacoil;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.dyonovan.modernalchemy.common.tileentity.TileModernAlchemy;
import com.dyonovan.modernalchemy.energy.SyncableRF;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.common.tileentity.BaseTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class TileTeslaStand extends TileModernAlchemy implements IEnergyHandler {

    protected SyncableRF energy;

    @Override
    public void onWrench(EntityPlayer player, int side) {
        if(getTileInDirection(ForgeDirection.UP) instanceof TileTeslaStand)
            ((TileTeslaStand)getTileInDirection(ForgeDirection.UP)).onWrench(player, side);
        else if(getTileInDirection(ForgeDirection.UP) instanceof TileTeslaCoil)
            ((TileTeslaCoil)getTileInDirection(ForgeDirection.UP)).onWrench(player, side);
    }

    @Override
    public boolean isActive() {
        return false;
    }
/*
    @Override
    public void returnWailaHead(List<String> tip) {
        int y = yCoord + 1;
        while(!worldObj.isAirBlock(xCoord, y, zCoord)) {
            if(worldObj.getBlock(xCoord, y, zCoord) == BlockHandler.blockTeslaCoil) {
                //((TileTeslaCoil) worldObj.getTileEntity(xCoord, y, zCoord)).returnWailaHead(tip);
                return;
            }
            else if(worldObj.getBlock(xCoord, y, zCoord) != BlockHandler.blockTeslaStand)
                return;
            y++;
        }
    }
*/

    @Override
    protected void createSyncedFields() {
        energy = new SyncableRF(new EnergyStorage(1000, 1000, 1000));
    }

    @Override
    public int receiveEnergy(ForgeDirection side, int maxReceive, boolean simulate) {
        return energy.getValue().receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection side, int maxReceive, boolean simulate) {
        return energy.getValue().extractEnergy(maxReceive, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return energy.getValue().getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return energy.getValue().getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection side) {
        return false;
    }

    @Override
    public void updateEntity() {
        if ((energy.getValue().getEnergyStored() > 0)) {
            TileEntity tile = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
            if (tile instanceof TileTeslaStand || tile instanceof TileTeslaCoil) {
                energy.getValue().extractEnergy(((IEnergyHandler) tile).receiveEnergy(ForgeDirection.DOWN, energy.getValue().extractEnergy(energy.getValue().getMaxExtract(), true), false), false);
            }
        }
    }
}
