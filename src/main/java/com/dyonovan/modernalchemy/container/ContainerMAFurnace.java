package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.machines.TileMAFurnace;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

public class ContainerMAFurnace extends BaseContainer {

    public ContainerMAFurnace(InventoryPlayer inventory, TileMAFurnace tileMAFurnace) {

        // Input
        addSlotToContainer(new Slot(tileMAFurnace, 0, 67, 26));
        addSlotToContainer(new Slot(tileMAFurnace, 1, 85, 26));
        addSlotToContainer(new Slot(tileMAFurnace, 2, 67, 44));
        addSlotToContainer(new Slot(tileMAFurnace, 3, 85, 44));

        // Output
        addSlotToContainer(new SlotFurnace(inventory.player, tileMAFurnace, 4, 143, 34));

        //player inv
        bindPlayerInventory(inventory, 8, 84);
    }
}
