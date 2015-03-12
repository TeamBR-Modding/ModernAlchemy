package com.dyonovan.modernalchemy.container;

import net.minecraft.inventory.IInventory;

public interface IInventoryCallback {
    public void onInventoryChanged(IInventory inventory, int slotNumber);
}
