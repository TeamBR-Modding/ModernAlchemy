package com.dyonovan.itemreplication.container;

import com.dyonovan.itemreplication.tileentity.TilePatternRecorder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Tim on 2/8/2015.
 */
public class ContainerPatternRecorder extends Container {

    private TilePatternRecorder tile;

    public ContainerPatternRecorder(InventoryPlayer playerInventory, TilePatternRecorder tileEntity){
        tile = tileEntity;

        // input
        addSlotToContainer(new Slot(tile, TilePatternRecorder.PATTERN_INPUT_SLOT, 44, 35));

        // item
        addSlotToContainer(new Slot(tile, TilePatternRecorder.ITEM_SLOT, 82, 35));

        // output
        addSlotToContainer(new Slot(tile, TilePatternRecorder.PATTERN_OUTPUT_SLOT, 142, 35));

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
    public ItemStack transferStackInSlot(EntityPlayer player, int fromSlot)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(fromSlot);
        System.out.println("");
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();

            if (fromSlot != 1 && fromSlot != 0) {
               /* if (itemstack1.getItem() == Items.coal) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return null;
                    }
                }*/

                if (!this.mergeItemStack(itemStack1, 0, 2, false)) {
                    return null;
                }
            }
            else if (fromSlot >= 2 && fromSlot < 31)
            {
                if (!this.mergeItemStack(itemStack1, 29, 38, false))
                {
                    return null;
                }
            }
            else if (fromSlot >= 29 && fromSlot < 38 && !this.mergeItemStack(itemStack1, 2, 29, false))
            {
                return null;
            }

            else if (!this.mergeItemStack(itemStack1, 2, 38, false))
            {
                return null;
            }

            if (itemStack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemStack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(player, itemStack1);
        }
        return itemstack;
    }
}
