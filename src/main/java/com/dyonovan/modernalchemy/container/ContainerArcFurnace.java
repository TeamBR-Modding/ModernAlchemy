package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.arcfurnace.TileArcFurnaceCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerArcFurnace extends Container {

    private TileArcFurnaceCore core;

    public ContainerArcFurnace(InventoryPlayer playerInventory, TileArcFurnaceCore tileArcFurnaceCore) {
        core = tileArcFurnaceCore;

        // Input
        addSlotToContainer(new Slot(core, 0, 72, 10));

        // Catalyst
        addSlotToContainer(new Slot(core, 1, 72, 60));
        bindPlayerInventory(playerInventory);
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
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 != 1 && par2 != 0) {
                if (itemstack1.getItem() == Items.coal) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return null;
                    }
                }

                else if (!this.mergeItemStack(itemstack1, 0, 2, false)) {
                    return null;
                }
            }
            else if (par2 >= 2 && par2 < 31)
            {
                if (!this.mergeItemStack(itemstack1, 29, 38, false))
                {
                    return null;
                }
            }
            else if (par2 >= 29 && par2 < 38 && !this.mergeItemStack(itemstack1, 2, 29, false))
            {
                return null;
            }

            else if (!this.mergeItemStack(itemstack1, 2, 38, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }
        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
