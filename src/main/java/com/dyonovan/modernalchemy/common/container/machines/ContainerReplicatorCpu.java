package com.dyonovan.modernalchemy.common.container.machines;

import com.dyonovan.modernalchemy.common.tileentity.replicator.TileReplicatorCPU;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import openmods.container.ContainerInventoryProvider;

public class ContainerReplicatorCpu extends ContainerInventoryProvider<TileReplicatorCPU> {

    public ContainerReplicatorCpu(IInventory playerInventory, TileReplicatorCPU owner) {

        super(playerInventory, owner);

        addSlotToContainer(new Slot(owner.getInventory(), 0, 44, 35));
        addSlotToContainer(new Slot(owner.getInventory(), 1, 82, 35));
        addSlotToContainer(new SlotFurnace(((InventoryPlayer)playerInventory).player, owner.getInventory(), 2, 142, 35));
        addPlayerInventorySlots(8, 84);
    }

}
