package com.dyonovan.modernalchemy.common.container.machines;

import com.dyonovan.modernalchemy.common.tileentity.arcfurnace.TileArcFurnaceCore;
import net.minecraft.inventory.IInventory;
import openmods.container.ContainerInventoryProvider;

public class ContainerArcFurnace extends ContainerInventoryProvider<TileArcFurnaceCore> {

    public ContainerArcFurnace(IInventory playerInventory, TileArcFurnaceCore owner) {
        super(playerInventory, owner);
        addSlotToContainer(new RestrictedSlot(owner.getInventory(), 0, 80, 22));
        addSlotToContainer(new RestrictedSlot(owner.getInventory(), 1, 80, 52));
        addPlayerInventorySlots(8, 84);
    }
}
