package com.dyonovan.itemreplication.tileentity;

import com.dyonovan.itemreplication.energy.ITeslaWorker;
import com.dyonovan.itemreplication.energy.TeslaBank;
import com.dyonovan.itemreplication.energy.TeslaMachine;
import com.dyonovan.itemreplication.handlers.ItemHandler;
import com.dyonovan.itemreplication.items.ItemPattern;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Created by Tim on 2/5/2015.
 */
public class TilePatternRecorder extends BaseTile implements ITeslaWorker, IInventory {

    public ItemStack inventory[];
    public static final int ITEM_SLOT = 0;
    public static final int PATTERN_INPUT_SLOT = 1;
    public static final int PATTERN_OUTPUT_SLOT = 2;

    private TeslaMachine teslaMachine;

    public TilePatternRecorder() {
        inventory = new ItemStack[3];
        teslaMachine = new TeslaMachine(this);
    }

    public TeslaBank getEnergyBank() {
        return teslaMachine.getEnergyBank();
    }

    public int getProgressScaled(int scale){
        return teslaMachine.getProgressScaled(scale);
    }

    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        teslaMachine.readFromNBT(tag);

        NBTTagList itemsTag = tag.getTagList("Items", 10);
        this.inventory = new ItemStack[getSizeInventory()];
        for (int i = 0; i < itemsTag.tagCount(); i++)
        {
            NBTTagCompound nbtTagCompound1 = itemsTag.getCompoundTagAt(i);
            NBTBase nbt = nbtTagCompound1.getTag("Slot");
            int j = -1;
            if ((nbt instanceof NBTTagByte)) {
                j = nbtTagCompound1.getByte("Slot") & 0xFF;
            } else {
                j = nbtTagCompound1.getShort("Slot");
            }
            if ((j >= 0) && (j < this.inventory.length)) {
                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbtTagCompound1);
            }
        }

     }

    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        teslaMachine.writeToNBT(tag);

        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < this.inventory.length; i++) {
            if (this.inventory[i] != null)
            {
                NBTTagCompound nbtTagCompound1 = new NBTTagCompound();
                nbtTagCompound1.setShort("Slot", (short)i);
                this.inventory[i].writeToNBT(nbtTagCompound1);
                nbtTagList.appendTag(nbtTagCompound1);
            }
        }
        tag.setTag("Items", nbtTagList);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        teslaMachine.update(worldObj, this);
    }

    @Override
    public boolean machineCanOperate() {
        return true; // no special conditions here
    }

    @Override
    public boolean canStartWork() {
        return inventory[PATTERN_INPUT_SLOT] != null && inventory[ITEM_SLOT] != null && inventory[PATTERN_OUTPUT_SLOT] != null &&
                inventory[PATTERN_INPUT_SLOT].getItem() == ItemHandler.itemPattern &&  // must have a pattern - doesn't matter if it is already recorded
                inventory[PATTERN_OUTPUT_SLOT].getItem() == null &&  // output must be empty
                inventory[ITEM_SLOT].getItem() != null; // must have an item - TODO: restrict items?
    }

    @Override
    public void onWorkStart() {
        // consume resources
        inventory[PATTERN_INPUT_SLOT].stackSize--;
        if(inventory[PATTERN_INPUT_SLOT].stackSize == 0)
            inventory[PATTERN_INPUT_SLOT] = null;
    }

    @Override
    public boolean doWork(int amount, int progress, int totalProcessTime) {
        return true;
    }

    @Override
    public void onWorkFinish() {
        // create products
        inventory[PATTERN_OUTPUT_SLOT] = new ItemStack(ItemHandler.itemPattern);
        ItemPattern.recordPattern(inventory[PATTERN_OUTPUT_SLOT], inventory[ITEM_SLOT]);
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
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public String getInventoryName() {
        return null;
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
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {

        switch(slot){
            case TilePatternRecorder.ITEM_SLOT:
                return stack.getItem() != ItemHandler.itemPattern; // TODO: how should this be limited?
            case TilePatternRecorder.PATTERN_INPUT_SLOT:
                return stack.getItem() == ItemHandler.itemPattern;
            default:
                return false;
        }
    }
}
