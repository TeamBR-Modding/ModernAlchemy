package com.dyonovan.modernalchemy.tileentity.machines;

import com.dyonovan.modernalchemy.blocks.machines.BlockSolidifier;
import com.dyonovan.modernalchemy.energy.ITeslaHandler;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.BaseTile;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.util.RenderUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.List;

public class TileSolidifier extends BaseTile implements IFluidHandler, ITeslaHandler, ISidedInventory {

    private static final int PROCESS_TIME = 500;

    public FluidTank tank;
    private TeslaBank energy;
    private int currentSpeed;
    public ItemStack inventory[];
    public int timeProcessed;

    public TileSolidifier() {
        tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
        tank.setFluid(new FluidStack(BlockHandler.fluidActinium, 0));
        this.energy = new TeslaBank(1000);
        this.isActive = false;
        inventory = new ItemStack[1];
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        energy.readFromNBT(tag);
        tank.readFromNBT(tag);
        timeProcessed = tag.getInteger("TimeProcessed");

        setInventorySlotContents(0, ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Item")));

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        energy.writeToNBT(tag);
        tank.writeToNBT(tag);
        tag.setInteger("TimeProcessed", timeProcessed);

        ItemStack itemstack = getStackInSlot(0);
        NBTTagCompound item = new NBTTagCompound();
        if (itemstack != null) {
            itemstack.writeToNBT(item);
        }
        tag.setTag("Item", item);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource.getFluid() != BlockHandler.fluidActinium) return 0;
        int amount = tank.fill(resource, doFill);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return amount;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (!resource.isFluidEqual(tank.getFluid()))
        {
            return null;
        }
        return tank.drain(resource.amount, true);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { tank.getInfo() };
    }

    @Override
    public void addEnergy(int maxAmount) {
        energy.addEnergy(maxAmount);
    }

    @Override
    public int drainEnergy(int maxAmount) {
        return energy.drainEnergy(maxAmount);
    }

    @Override
    public int getEnergyLevel() {
        return energy.getEnergyLevel();
    }

    @Override
    public TeslaBank getEnergyBank() {
        return energy;
    }

    public void setEnergy(int amount) {
        energy.setEnergyLevel(amount);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (worldObj.isRemote) return;

        if (energy.canAcceptEnergy()) {
            chargeFromCoils();
        }


            if (this.inventory[0] != null && this.inventory[0].stackSize >= 64) return;
            if (energy.getEnergyLevel() > 0  && isPowered()) {
                updateSpeed();
                if (timeProcessed == 0 && tank.getFluidAmount() > 0) {
                    BlockSolidifier.toggleIsActive(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                    isActive = true;
                    timeProcessed = 1;
                }
                if (timeProcessed > 0 && timeProcessed < PROCESS_TIME) {
                    if (energy.getEnergyLevel() > 0 && (tank.getFluid() != null || tank.getFluidAmount() > 10 * currentSpeed)) {
                        energy.drainEnergy(currentSpeed);
                        tank.drain(10 * currentSpeed, true);
                        timeProcessed += currentSpeed;
                    } else {
                        doReset();
                        return;
                    }
                }

                if (timeProcessed >= PROCESS_TIME) {
                    if (inventory[0] == null) setInventorySlotContents(0, new ItemStack(ItemHandler.itemReplicationMedium));
                    else inventory[0].stackSize++;
                    doReset();
                }
                super.markDirty();
            } else {
                doReset();
            }
    }

    private void doReset() {

        if (isActive()) {
            BlockSolidifier.toggleIsActive(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            isActive = false;
            timeProcessed = 0;
        }
    }

    public void updateSpeed() {
        if(energy.getEnergyLevel() == 0) {
            currentSpeed = 0;
            return;
        }

        currentSpeed = (energy.getEnergyLevel() * 20) / energy.getMaxCapacity();
        if(currentSpeed == 0)
            currentSpeed = 1;
    }

    public void chargeFromCoils() {
        int maxFill = energy.getMaxCapacity() - energy.getEnergyLevel();
        List<TileTeslaCoil> coils = findCoils(worldObj);
        int currentDrain = 0;
        for (TileTeslaCoil coil : coils) {
            if (coil.getEnergyLevel() <= 0) continue;
            int fill = coil.getEnergyLevel() > ConfigHandler.maxCoilTransfer ? ConfigHandler.maxCoilTransfer : coil.getEnergyLevel();
            if (currentDrain + fill > maxFill)
                fill = maxFill - currentDrain;
            currentDrain += fill;
            coil.drainEnergy(fill);

            RenderUtils.sendBoltToClient(xCoord, yCoord, zCoord, coil, fill);
        }
        while (currentDrain > 0) {
            energy.addEnergy(ConfigHandler.maxCoilTransfer);
            currentDrain--;
        }
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return new int[] {0};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
        return false;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return true;
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        ItemStack itemstack = getStackInSlot(slot);

        if(itemstack != null) {
            if(itemstack.stackSize <= count) {
                setInventorySlotContents(slot, null);
            }
            itemstack = itemstack.splitStack(count);

        }
        super.markDirty();
        return itemstack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack itemStack = getStackInSlot(slot);
        setInventorySlotContents(slot, null);
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        inventory[slot] = itemstack;
    }

    @Override
    public String getInventoryName() {
        return Constants.MODID + ":blockSolidifier";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return true;
    }



    public int getCookTimeScaled(int scale) {
        return timeProcessed == 0 ? 0 : timeProcessed * scale / PROCESS_TIME;
    }
}
