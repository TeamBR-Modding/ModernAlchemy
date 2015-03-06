package com.dyonovan.modernalchemy.tileentity.teslacoil;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import com.dyonovan.modernalchemy.tileentity.BaseTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class TileTeslaBase extends BaseTile implements IEnergyHandler {

    protected EnergyStorage energyRF = new EnergyStorage(1000, 1000, 1000);

    public TileTeslaBase() {

    }

    public boolean isCoilCharging() {
        int y = yCoord + 1;
        while(!worldObj.isAirBlock(xCoord, y, zCoord)) {
            if (worldObj.getBlock(xCoord, y, zCoord) == BlockHandler.blockTeslaStand) {
                y++;
            } else
                return worldObj.getBlock(xCoord, y, zCoord) == BlockHandler.blockCoil && ((TileTeslaCoil) worldObj.getTileEntity(xCoord, y, zCoord)).isActive();
        }
        return false;
    }

    @Override
    public void onWrench(EntityPlayer player) {
        if(getTileInDirection(ForgeDirection.UP) instanceof TileTeslaStand)
            ((TileTeslaStand)getTileInDirection(ForgeDirection.UP)).onWrench(player);
    }

    @Override
    public void returnWailaHead(List<String> tip) {
        tip.add(GuiHelper.GuiColor.YELLOW + "Is Converting: " + GuiHelper.GuiColor.WHITE + (isCoilCharging() ? "Yes" : "No"));
    }

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
    public int receiveEnergy(ForgeDirection side, int maxReceive, boolean simulate) {
        return energyRF.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection side, int maxReceive, boolean simulate) {
        return energyRF.extractEnergy(maxReceive, simulate);
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
    public boolean canConnectEnergy(ForgeDirection side) {
        return side != ForgeDirection.UP;
    }

    @Override
    public void updateEntity() {
        if ((energyRF.getEnergyStored() > 0)) {

            TileEntity tile = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
            if (tile instanceof TileTeslaStand) {
                energyRF.extractEnergy(((IEnergyHandler) tile).receiveEnergy(ForgeDirection.DOWN, energyRF.extractEnergy(energyRF.getMaxExtract(), true), false), false);
            }
        }
    }
}
