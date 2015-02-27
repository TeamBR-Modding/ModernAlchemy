package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.machines.TileAmalgamator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.SlotFurnace;

public class ContainerAmalgamator extends BaseContainer {

    public ContainerAmalgamator(InventoryPlayer inventory, TileAmalgamator tile) {
        addSlotToContainer(new SlotFurnace(inventory.player, tile, 0, 146, 34));
        bindPlayerInventory(inventory, 8, 84);
        setCanSendToTile(false);
    }
}
