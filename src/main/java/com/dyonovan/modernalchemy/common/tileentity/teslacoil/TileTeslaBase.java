package com.dyonovan.modernalchemy.common.tileentity.teslacoil;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.dyonovan.modernalchemy.common.blocks.teslacoil.BlockTeslaBase;
import com.dyonovan.modernalchemy.common.tileentity.TileModernAlchemy;
import com.dyonovan.modernalchemy.energy.SyncableRF;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.common.tileentity.BaseTile;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import com.dyonovan.modernalchemy.util.Location;
import com.dyonovan.modernalchemy.util.WorldUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class TileTeslaBase extends TileModernAlchemy implements IEnergyHandler {

    protected SyncableRF energyRF;

    @Override
    protected void createSyncedFields() {
        energyRF = new SyncableRF(new EnergyStorage(1000, 1000, 1000));
    }

    public boolean isCoilCharging() {
        int y = yCoord + 1;
        while(!worldObj.isAirBlock(xCoord, y, zCoord)) {
            if (worldObj.getBlock(xCoord, y, zCoord) == BlockHandler.blockTeslaStand) {
                y++;
            } else
                return (worldObj.getBlock(xCoord, y, zCoord) == BlockHandler.blockTeslaCoil &&
                        ((TileTeslaCoil) worldObj.getTileEntity(xCoord, y, zCoord)).isActive())
                        || (worldObj.getBlock(xCoord, y, zCoord) == BlockHandler.blockSuperTeslaCoil &&
                        ((TileSuperTeslaCoil) worldObj.getTileEntity(xCoord, y, zCoord)).isActive());
        }
        return false;
    }

    @Override
    public void onWrench(EntityPlayer player, int side) {
        if(getTileInDirection(ForgeDirection.UP) instanceof TileTeslaStand)
            ((TileTeslaStand)getTileInDirection(ForgeDirection.UP)).onWrench(player, side);
    }

    @Override
    public boolean isActive() {
        return isCoilCharging();
    }
/*
    @Override
    public void returnWailaHead(List<String> tip) {
        tip.add(GuiHelper.GuiColor.YELLOW + "Is Converting: " + GuiHelper.GuiColor.WHITE + (isCoilCharging() ? "Yes" : "No"));
    }
*/

    @Override
    public int receiveEnergy(ForgeDirection side, int maxReceive, boolean simulate) {
        return energyRF.getValue().receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection side, int maxReceive, boolean simulate) {
        return energyRF.getValue().extractEnergy(maxReceive, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return energyRF.getValue().getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return energyRF.getValue().getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection side) {
        return side != ForgeDirection.UP;
    }

    @Override
    public void updateEntity() {
        if ((energyRF.getValue().getEnergyStored() > 0)) {

            TileEntity tile = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
            if (tile instanceof TileTeslaStand) {
                energyRF.getValue().extractEnergy(((IEnergyHandler) tile).receiveEnergy(ForgeDirection.DOWN, energyRF.getValue().extractEnergy(energyRF.getValue().getMaxExtract(), true), false), false);
            }
        }
    }
}
