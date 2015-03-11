package com.dyonovan.modernalchemy.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.List;

public abstract class BaseTile extends TileEntity {

    public abstract void onWrench(EntityPlayer player);

    public boolean isPowered() {
        return isPoweringTo(worldObj, xCoord, yCoord + 1, zCoord, 0) ||
                isPoweringTo(worldObj, xCoord, yCoord - 1, zCoord, 1) ||
                isPoweringTo(worldObj, xCoord, yCoord, zCoord + 1, 2) ||
                isPoweringTo(worldObj, xCoord, yCoord, zCoord - 1, 3) ||
                isPoweringTo(worldObj, xCoord + 1, yCoord, zCoord, 4) ||
                isPoweringTo(worldObj, xCoord - 1, yCoord, zCoord, 5);
    }

    public static boolean isPoweringTo(World world, int x, int y, int z, int side) {
        return world.getBlock(x, y, z).isProvidingWeakPower(world, x, y, z, side) > 0;
    }

    public TileEntity getTileInDirection(ForgeDirection direction) {
        int x = xCoord + direction.offsetX;
        int y = yCoord + direction.offsetY;
        int z = zCoord + direction.offsetZ;

        if (worldObj != null && worldObj.blockExists(x, y, z)) { return worldObj.getTileEntity(x, y, z); }
        return null;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.func_148857_g());
    }


    /*******************************************************************************************************************
     ********************************************* Fluid Functions *****************************************************
     *******************************************************************************************************************/

    public void distributeFluids(FluidTank tank, boolean single, ForgeDirection... directions) {
        if(tank.getFluid() != null) {
            for (ForgeDirection dir : directions) {
                if (getTileInDirection(dir) != null && getTileInDirection(dir) instanceof IFluidHandler) {
                    IFluidHandler otherTank = (IFluidHandler) getTileInDirection(dir);
                    if (tank.getFluid() != null && otherTank.canFill(dir, tank.getFluid().getFluid())) {
                        tank.drain(otherTank.fill(dir.getOpposite(), new FluidStack(tank.getFluid().getFluid(), 50), true), true);
                        if (single)
                            return;
                    }
                }
            }
        }
    }

    public void distributeFluids(FluidTank tank, boolean single) {
        distributeFluids(tank, single, ForgeDirection.VALID_DIRECTIONS);
    }

    public void distributeFluids(FluidTank tank) {
        distributeFluids(tank, true, ForgeDirection.VALID_DIRECTIONS);
    }

    public abstract void returnWailaHead(List<String> tip);
}