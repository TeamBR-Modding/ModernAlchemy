package com.dyonovan.modernalchemy.tileentity;

import com.dyonovan.modernalchemy.util.InventoryUtils;
import com.dyonovan.modernalchemy.util.WorldUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class BaseTile extends TileEntity {

    public abstract void onWrench(EntityPlayer player, int side);

    public HashMap<ForgeDirection, IInputOutput.MODE> inputOutputModes;

    public void initIOModes() {
        inputOutputModes = new HashMap<>();
        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
            inputOutputModes.put(dir, IInputOutput.MODE.NONE);
    }

    public ForgeDirection convertSideToDirection(int side) {
        switch (side) {
            case 0 :
                return ForgeDirection.DOWN;
            case 1 :
                return ForgeDirection.UP;
            case 2 :
                return ForgeDirection.NORTH;
            case 3 :
                return ForgeDirection.SOUTH;
            case 4 :
                return ForgeDirection.WEST;
            case 5 :
                return ForgeDirection.EAST;
            default :
                return ForgeDirection.UNKNOWN;

        }
    }

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

    public void exportFluids(FluidTank tank, boolean single, ForgeDirection... directions) {
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

    public void exportFluids(FluidTank tank, boolean single) {
        exportFluids(tank, single, ForgeDirection.VALID_DIRECTIONS);
    }

    public void exportFluids(FluidTank tank) {
        exportFluids(tank, true, ForgeDirection.VALID_DIRECTIONS);
    }

    public void importFluids(FluidTank tank, Fluid fillFluid, boolean single, ForgeDirection... directions) {
        for (ForgeDirection dir : directions) {
            if (getTileInDirection(dir) != null && getTileInDirection(dir) instanceof IFluidHandler) {
                IFluidHandler otherTank = (IFluidHandler) getTileInDirection(dir);
                if(otherTank.canDrain(dir.getOpposite(), fillFluid)) {
                    otherTank.drain(dir.getOpposite(), tank.fill(new FluidStack(fillFluid, 50), true), true);
                    if(single)
                        return;
                }
            }
        }
    }

    public void importFluids(FluidTank tank, Fluid fillFluid, boolean single) {
        importFluids(tank, fillFluid, single, ForgeDirection.VALID_DIRECTIONS);
    }

    public void importFluids(FluidTank tank, Fluid fillFluid) {
        importFluids(tank, fillFluid, true, ForgeDirection.VALID_DIRECTIONS);
    }

    public void expelItems(InventoryTile inventory) {
        for(ItemStack stack : inventory.getValues()) {
            if(stack != null) {
                WorldUtils.expelItem(worldObj, xCoord, yCoord, zCoord, stack);
            }
        }
    }

    /*******************************************************************************************************************
     ********************************************** Item Functions *****************************************************
     *******************************************************************************************************************/

    public void importItems(IInventory pullTo, int inputSlot, int maxAmount, boolean single, ForgeDirection[] dirs, Item...filter) {
        List<Item> filteredItems = new ArrayList<>(Arrays.asList(filter));
        for(ForgeDirection dir : dirs) {
            if(getTileInDirection(dir) != null && getTileInDirection(dir) instanceof IInventory) {
                IInventory other = (IInventory)getTileInDirection(dir);
                for(int i = 0; i < other.getSizeInventory(); i++) {
                    if(other.getStackInSlot(i) != null) {
                        if(filteredItems.isEmpty() || filteredItems.contains(other.getStackInSlot(i).getItem())) {
                            if(InventoryUtils.moveItemInto(other, i, pullTo, inputSlot, maxAmount, dir.getOpposite(), true, true) > 0 && single)
                                return;
                        }
                    }
                }
            }
        }
    }

    public void exportItems(IInventory pullFrom, int outputSlot, int maxAmount, boolean single, ForgeDirection[] dirs) {
        for(ForgeDirection dir : dirs) {
            if(getTileInDirection(dir) != null && getTileInDirection(dir) instanceof IInventory) {
                IInventory other = (IInventory)getTileInDirection(dir);
                if(InventoryUtils.moveItemInto(pullFrom, outputSlot, other, -1, maxAmount, dir.getOpposite(), true, true) > 0 && single)
                    return;

            }
        }
    }

    public abstract void returnWailaHead(List<String> tip);
}