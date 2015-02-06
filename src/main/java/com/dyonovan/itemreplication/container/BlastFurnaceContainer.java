package com.dyonovan.itemreplication.container;

import com.dyonovan.itemreplication.tileentity.TileBlastFurnaceCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class BlastFurnaceContainer extends Container {

    private TileBlastFurnaceCore core;

    public BlastFurnaceContainer(InventoryPlayer playerInventory, TileBlastFurnaceCore tileBlastFurnaceCore) {
        super();
        core = tileBlastFurnaceCore;
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
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
