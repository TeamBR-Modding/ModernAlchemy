package com.dyonovan.itemreplication.container;

import com.dyonovan.itemreplication.tileentity.machines.TilePatternRecorder;
import com.dyonovan.itemreplication.tileentity.replicator.TileReplicatorCPU;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerReplicatorCpu extends Container {

    private TileReplicatorCPU tile;
    private int lastPower, currentProcessTime, requiredProcessTime;

    public ContainerReplicatorCpu(InventoryPlayer playerInventory, TileReplicatorCPU tileEntity) {

        tile = tileEntity;

        addSlotToContainer(new Slot(tile, 0, 44, 35));
        addSlotToContainer(new Slot(tile, 1, 82, 35));
        addSlotToContainer(new SlotFurnace(playerInventory.player, tile, 2, 142, 35));
        bindPlayerInventory(playerInventory);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    private void bindPlayerInventory(InventoryPlayer playerInventory)
    {
        // Inventory
        for(int y = 0; y < 3; y++)
            for(int x = 0; x < 9; x++)
                addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));

        // Action Bar
        for(int x = 0; x < 9; x++)
            addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 142));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {

        Slot slot = getSlot(i);

        if(slot != null && slot.getHasStack()) {

            if(!this.tile.isItemValidForSlot(i, slot.getStack())) return null;
            ItemStack itemstack = slot.getStack();
            ItemStack result = itemstack.copy();

            if(i >= 36) {
                if(!mergeItemStack(itemstack, 0, 36, false)) {
                    return null;
                }
            } else if(!mergeItemStack(itemstack, 36, 36 + tile.getSizeInventory(), false)) {
                return null;
            }

            if(itemstack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            slot.onPickupFromSlot(player, itemstack);
            return result;
        }
        return null;
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafter) {
        super.addCraftingToCrafters(crafter);
        crafter.sendProgressBarUpdate(this, 0, this.tile.getEnergyLevel());
        crafter.sendProgressBarUpdate(this, 1, this.tile.currentProcessTime);
        crafter.sendProgressBarUpdate(this, 2, this.tile.requiredProcessTime);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (Object crafter : this.crafters) {
            ICrafting icrafting = (ICrafting) crafter;
            if (this.lastPower != this.tile.getEnergyLevel())
                icrafting.sendProgressBarUpdate(this, 0, this.tile.getEnergyLevel());
            if (this.currentProcessTime != this.tile.currentProcessTime)
                icrafting.sendProgressBarUpdate(this, 1, this.tile.currentProcessTime);
            if (this.requiredProcessTime != this.tile.requiredProcessTime)
                icrafting.sendProgressBarUpdate(this, 2, this.tile.requiredProcessTime);
        }

        this.lastPower = this.tile.getEnergyLevel();
        this.currentProcessTime = this.tile.currentProcessTime;
        this.requiredProcessTime = this.tile.requiredProcessTime;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int i, int j) {

        super.updateProgressBar(i, j);

        switch (i) {
            case 0:
                this.tile.setEnergy(j);
                break;
            case 1:
                this.tile.currentProcessTime = j;
                break;
            case 2:
                this.requiredProcessTime = j;
                break;
        }
    }
}
