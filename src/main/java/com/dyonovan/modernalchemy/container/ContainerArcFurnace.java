package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.arcfurnace.TileArcFurnaceCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerArcFurnace extends BaseContainer {

    private TileArcFurnaceCore core;

    public ContainerArcFurnace(InventoryPlayer playerInventory, TileArcFurnaceCore tileArcFurnaceCore) {
        core = tileArcFurnaceCore;

        // Input
        addSlotToContainer(new Slot(core, 0, 72, 10));

        // Catalyst
        addSlotToContainer(new Slot(core, 1, 72, 60));
        bindPlayerInventory(playerInventory, 8, 84);
    }
}
