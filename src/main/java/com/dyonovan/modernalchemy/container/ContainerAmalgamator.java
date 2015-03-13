package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.machines.TileAmalgamator;
import com.dyonovan.teambrcore.container.BaseContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotFurnace;
import openmods.container.ContainerInventoryProvider;

public class ContainerAmalgamator extends ContainerInventoryProvider<TileAmalgamator> {

    public ContainerAmalgamator(IInventory playerInventory, TileAmalgamator owner) {
        super(playerInventory, owner);
        addSlotToContainer(new SlotFurnace(((InventoryPlayer)playerInventory).player, owner.getInventory(), 0, 146, 34));
        addPlayerInventorySlots(8, 84);
    }
}
