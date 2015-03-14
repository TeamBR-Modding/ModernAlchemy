package com.dyonovan.modernalchemy.common.container.machines;

import com.dyonovan.modernalchemy.common.container.BaseContainer;
import com.dyonovan.modernalchemy.common.tileentity.replicator.TileReplicatorCPU;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

public class ContainerReplicatorCpu extends BaseContainer {

    private TileReplicatorCPU tile;

    public ContainerReplicatorCpu(InventoryPlayer playerInventory, TileReplicatorCPU tileEntity) {
        tile = tileEntity;

        addSlotToContainer(new Slot(tile, 0, 44, 35));
        addSlotToContainer(new Slot(tile, 1, 82, 35));
        addSlotToContainer(new SlotFurnace(playerInventory.player, tile, 2, 142, 35));
        bindPlayerInventory(playerInventory, 8, 84);
    }

}
