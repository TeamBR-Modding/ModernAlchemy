package com.dyonovan.itemreplication.container;

import com.dyonovan.itemreplication.tileentity.TileArcFurnaceCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerArcFurnace extends Container {

    private TileArcFurnaceCore core;

    public ContainerArcFurnace(InventoryPlayer playerInventory, TileArcFurnaceCore tileArcFurnaceCore) {
        super();
        core = tileArcFurnaceCore;
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
