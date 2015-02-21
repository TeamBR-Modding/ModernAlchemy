package com.dyonovan.modernalchemy.tileentity.arcfurnace.dummies;

import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.TileArcFurnaceCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileDummyItemIO extends TileDummy implements ISidedInventory {

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[] {0, 1};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        TileArcFurnaceCore core = (TileArcFurnaceCore) getCore();
        if(core != null) {
            switch (slot) {
            case 0:
                if (item.getItem() == ItemHandler.itemActinium) {
                    return core.inventory.getStackInSlot(slot) == null || item.stackSize + core.inventory.getStackInSlot(slot).stackSize <= 64;
                }
                break;
            case 1:
                if (item.getItem() == Items.coal) {
                    return core.inventory.getStackInSlot(slot) == null || item.stackSize + core.inventory.getStackInSlot(slot).stackSize <= 64;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return getCore() != null ? 2 : 0;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        TileArcFurnaceCore core = (TileArcFurnaceCore) getCore();
        return core != null ? core.inventory.getStackInSlot(slot) : null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        TileArcFurnaceCore core = (TileArcFurnaceCore) getCore();
        if(core != null) {
            core.inventory.getStackInSlot(slot).stackSize -= amount;
            if(core.inventory.getStackInSlot(slot).stackSize <= 0) {
                core.inventory.setStackInSlot(null, slot);
            }
            return core.inventory.getStackInSlot(slot);
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        TileArcFurnaceCore core = (TileArcFurnaceCore) getCore();
        if(core != null) {
            return core.inventory.getStackInSlot(slot);
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        TileArcFurnaceCore core = (TileArcFurnaceCore) getCore();
        if(core != null) {
            core.inventory.setStackInSlot(stack, slot);
        }
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
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
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
        TileArcFurnaceCore core = (TileArcFurnaceCore) getCore();
        if(core != null) {
            return core.isItemValidForSlot(slot, stack);
        }
        return false;
    }
}
