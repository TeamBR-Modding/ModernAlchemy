package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.machines.TileAdvancedCrafter;
import com.dyonovan.teambrcore.container.BaseContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import openmods.container.ContainerInventoryProvider;

public class ContainerAdvancedCrafter extends ContainerInventoryProvider<TileAdvancedCrafter> {

    public ContainerAdvancedCrafter(IInventory playerInventory, TileAdvancedCrafter owner) {

        super(playerInventory, owner);
        // Input
        addSlotToContainer(new Slot(owner.getInventory(), 0, 67, 26));
        addSlotToContainer(new Slot(owner.getInventory(), 1, 85, 26));
        addSlotToContainer(new Slot(owner.getInventory(), 2, 67, 44));
        addSlotToContainer(new Slot(owner.getInventory(), 3, 85, 44));

        // Output
        addSlotToContainer(new SlotFurnace(((InventoryPlayer)playerInventory).player, owner.getInventory(), 4, 146, 34));

        //player inv
        addPlayerInventorySlots(8, 84);
    }
}
