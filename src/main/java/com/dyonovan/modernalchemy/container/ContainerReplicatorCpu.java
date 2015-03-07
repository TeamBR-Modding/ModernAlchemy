package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorCPU;
import com.dyonovan.teambrcore.container.BaseContainer;
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
