package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.arcfurnace.TileArcFurnaceCore;
import com.dyonovan.teambrcore.container.BaseContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import openmods.container.ContainerInventoryProvider;

public class ContainerArcFurnace extends ContainerInventoryProvider<TileArcFurnaceCore> {

    public ContainerArcFurnace(IInventory playerInventory, TileArcFurnaceCore owner) {
        super(playerInventory, owner);
        addSlotToContainer(new RestrictedSlot(owner.getInventory(), 0, 80, 22));
        addSlotToContainer(new RestrictedSlot(owner.getInventory(), 1, 80, 52));
        addPlayerInventorySlots(8, 84);
    }
}
