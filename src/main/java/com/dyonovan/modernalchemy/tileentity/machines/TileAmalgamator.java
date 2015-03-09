package com.dyonovan.modernalchemy.tileentity.machines;

import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.teambrcore.helpers.GuiHelper;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.BaseMachine;
import com.dyonovan.modernalchemy.tileentity.InventoryTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.List;

public class TileAmalgamator extends BaseMachine implements IFluidHandler, ISidedInventory {

    private static final int PROCESS_TIME = 500;

    public FluidTank tank;
    private int currentSpeed;
    private InventoryTile inventory;
    public int timeProcessed;

    public TileAmalgamator() {
        tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
        tank.setFluid(new FluidStack(BlockHandler.fluidActinium, 0));
        this.energyTank = new TeslaBank(1000);
        inventory = new InventoryTile(1);
    }

    /*******************************************************************************************************************
     ****************************************** Solidifier Functions ***************************************************
     *******************************************************************************************************************/

    private void doReset() {
        isActive = false;
        timeProcessed = 0;
        currentSpeed = 0;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    private void updateSpeed() {
        if(energyTank.getEnergyLevel() == 0) {
            currentSpeed = 0;
            return;
        }

        currentSpeed = (energyTank.getEnergyLevel() * 20) / energyTank.getMaxCapacity();
        if(currentSpeed == 0)
            currentSpeed = 1;
    }

    private void doSolidify() {
        if (canSolidify()) { //Must have power and redstone signal
            updateSpeed();
            if (timeProcessed == 0 && tank.getFluidAmount() > 0) { //Set the block to active and continue
                isActive = true;
                timeProcessed += 1;
            }
            if (timeProcessed > 0 && timeProcessed < PROCESS_TIME) { //Still cooking
                if (tank.getFluid() != null) { //Drain until there is nothing left
                    energyTank.drainEnergy(currentSpeed);
                    tank.drain(5, true);
                    timeProcessed += currentSpeed;
                }
                else
                    doReset();
            }

            if (timeProcessed >= PROCESS_TIME) { //Completed
                if (inventory.getStackInSlot(0) == null)
                    setInventorySlotContents(0, new ItemStack(ItemHandler.itemReplicationMedium));
                else
                    inventory.getStackInSlot(0).stackSize++;
                doReset();
            }
        } else {
            doReset();
        }
    }

    private boolean canSolidify() {
        if(this.inventory.getStackInSlot(0) != null && this.inventory.getStackInSlot(0).stackSize >= 64) //Return false if there is no room
            return false;

        return energyTank.getEnergyLevel() > 0 && isPowered();
    }

    public int getCookTimeScaled(int scale) {
        return timeProcessed * scale / PROCESS_TIME;
    }

    /*******************************************************************************************************************
     ********************************************* Fluid Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource.getFluid() != BlockHandler.fluidActinium) return 0;
        int amount = tank.fill(resource, doFill);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return amount;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
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
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { tank.getInfo() };
    }

    /*******************************************************************************************************************
     ********************************************** Item Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
        return false;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return new int[] {0};
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack itemStack = getStackInSlot(slot);
        setInventorySlotContents(slot, null);
        return itemStack;
    }

    @Override
    public String getInventoryName() {
        return Constants.MODID + ":blockAmalgamator";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
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
    public void setInventorySlotContents(int slot, ItemStack itemstack){ inventory.setStackInSlot(itemstack, slot); }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    /*******************************************************************************************************************
     ********************************************** Tile Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) return;

        if (energyTank.canAcceptEnergy()) {
            chargeFromCoils();
        }
        doSolidify();
        super.updateEntity();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        tank.readFromNBT(tag);
        timeProcessed = tag.getInteger("TimeProcessed");
        currentSpeed = tag.getInteger("CurrentSpeed");
        inventory.readFromNBT(tag, this);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tank.writeToNBT(tag);
        tag.setInteger("TimeProcessed", timeProcessed);
        tag.setInteger("CurrentSpeed", currentSpeed);
        inventory.writeToNBT(tag);
    }

    /*******************************************************************************************************************
     ********************************************** Misc Functions *****************************************************
     *******************************************************************************************************************/
    @Override
    public void returnWailaHead(List<String> head) {
        head.add(GuiHelper.GuiColor.YELLOW + "Is Amalgamating: " + GuiHelper.GuiColor.WHITE + (isActive() ? "Yes" : "No"));
        head.add(GuiHelper.GuiColor.YELLOW + "Energy: " + GuiHelper.GuiColor.WHITE + energyTank.getEnergyLevel() + "/" + energyTank.getMaxCapacity() + GuiHelper.GuiColor.TURQUISE + "T");
        head.add(GuiHelper.GuiColor.YELLOW + "Actinium: " + GuiHelper.GuiColor.WHITE + tank.getFluidAmount() + "/" + tank.getCapacity() + GuiHelper.GuiColor.TURQUISE + "mb");
        head.add(GuiHelper.GuiColor.YELLOW + "Current Speed: " + GuiHelper.GuiColor.WHITE + currentSpeed);
    }
}
