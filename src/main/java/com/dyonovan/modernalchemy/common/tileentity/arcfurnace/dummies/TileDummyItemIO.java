package com.dyonovan.modernalchemy.common.tileentity.arcfurnace.dummies;

import com.dyonovan.modernalchemy.common.blocks.arcfurnace.dummies.BlockItemIODummy;
import com.dyonovan.modernalchemy.crafting.ArcFurnaceRecipeRegistry;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.common.tileentity.arcfurnace.TileArcFurnaceCore;
import com.dyonovan.modernalchemy.util.InventoryUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.api.IIconProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileDummyItemIO extends TileDummy implements ISidedInventory, IIconProvider {

    @Override
    public IIcon getIcon(ForgeDirection rotatedDir) {
        return getCore() != null ? BlockItemIODummy.Icons.active : BlockItemIODummy.Icons.inActive;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return new int[] {TileArcFurnaceCore.CATALYST_SLOT, TileArcFurnaceCore.INPUT_SLOT};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        if(getCore() != null) {
            switch (slot) {
                case 0:
                    if (ArcFurnaceRecipeRegistry.instance.getReturn(item.getItem()) > 0) {
                        return getCore().getInventory().getStackInSlot(slot) == null || item.stackSize + getCore().getInventory().getStackInSlot(slot).stackSize <= 64;
                    }
                    break;
                case 1:
                    if (item.getItem() == Items.coal) {
                        return getCore().getInventory().getStackInSlot(slot) == null || item.stackSize + getCore().getInventory().getStackInSlot(slot).stackSize <= 64;
                    }
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return getCore() != null ? 2 : 0;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return getCore() != null ? getCore().getInventory().getStackInSlot(slot) : null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if(getCore() != null) {
            getCore().getInventory().getStackInSlot(slot).stackSize -= amount;
            if(getCore().getInventory().getStackInSlot(slot).stackSize <= 0) {
                getCore().getInventory().setInventorySlotContents(slot, null);
            }
            return getCore().getInventory().getStackInSlot(slot);
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if(getCore() != null) {
            return getCore().getInventory().getStackInSlot(slot);
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if(getCore() != null) {
            getCore().getInventory().setInventorySlotContents(slot, stack);
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
        if(getCore() != null) {
            return getCore().getInventory().isItemValidForSlot(slot, stack);
        }
        return false;
    }

    @Override
    public void updateEntity() {
        if(getCore() != null) {
            importItems(getCore().getInventory(), TileArcFurnaceCore.INPUT_SLOT, 1, false, ForgeDirection.VALID_DIRECTIONS, Item.getItemFromBlock(BlockHandler.blockOreActinium), ItemHandler.itemSlag, ItemHandler.itemActiniumDust);
            importItems(getCore().getInventory(), TileArcFurnaceCore.CATALYST_SLOT, 1, false, ForgeDirection.VALID_DIRECTIONS, Items.coal);
        }
    }

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
}
