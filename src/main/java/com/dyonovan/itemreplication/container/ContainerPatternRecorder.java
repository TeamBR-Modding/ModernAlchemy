package com.dyonovan.itemreplication.container;

import com.dyonovan.itemreplication.tileentity.TilePatternRecorder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerPatternRecorder extends Container {

    private TilePatternRecorder tile;

    public ContainerPatternRecorder(InventoryPlayer playerInventory, TilePatternRecorder tileEntity){
        tile = tileEntity;

        addSlotToContainer(new Slot(tile, TilePatternRecorder.INPUT_SLOT, 44, 35));
        addSlotToContainer(new Slot(tile, TilePatternRecorder.ITEM_SLOT, 82, 35));
        addSlotToContainer(new SlotFurnace(playerInventory.player, tile, TilePatternRecorder.OUTPUT_SLOT, 142, 35));
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

}
