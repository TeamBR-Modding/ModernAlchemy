package com.dyonovan.itemreplication.tileentity;

import com.dyonovan.itemreplication.items.ItemCube;
import com.dyonovan.itemreplication.lib.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileReplicatorStand extends BaseTile implements ISidedInventory {

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    public InventoryTile inventory;

    public TileReplicatorStand() {
        inventory = new InventoryTile(2);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        inventory.readFromNBT(tag, this);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        inventory.writeToNBT(tag);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int i) {
        return new int[] {0, 1};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
        return !(slot == 1 || side != 0);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
        return !(slot == 0 || side != 0);
    }

    @Override
    public int getSizeInventory() {
        return 2;
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
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack itemStack = getStackInSlot(slot);
        setInventorySlotContents(slot, null);
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        inventory.setStackInSlot(itemStack, slot);
        worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public String getInventoryName() {
        return Constants.MODID + ":blockReplicatorStand";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return slot == 0 && itemStack.getItem() instanceof ItemCube;
    }
}
