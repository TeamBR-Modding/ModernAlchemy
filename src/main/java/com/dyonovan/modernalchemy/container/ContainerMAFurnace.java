package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.machines.TileMAFurnace;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

public class ContainerMAFurnace extends BaseContainer {

    public ContainerMAFurnace(InventoryPlayer inventory, TileMAFurnace tileMAFurnace) {

        // Input
        addSlotToContainer(new Slot(tileMAFurnace, 0, 72, 10));
        addSlotToContainer(new Slot(tileMAFurnace, 1, 72, 10));
        addSlotToContainer(new Slot(tileMAFurnace, 2, 72, 10));
        addSlotToContainer(new Slot(tileMAFurnace, 3, 72, 10));

        // Output
        addSlotToContainer(new SlotFurnace(inventory.player, tileMAFurnace, 4, 72, 60));

        //player inv
        bindPlayerInventory(inventory, 8, 84);

    }
}
