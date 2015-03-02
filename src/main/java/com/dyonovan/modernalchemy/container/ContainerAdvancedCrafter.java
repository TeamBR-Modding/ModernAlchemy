package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.machines.TileAdvancedCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

public class ContainerAdvancedCrafter extends BaseContainer {

    TileAdvancedCrafter tile;

    public ContainerAdvancedCrafter(InventoryPlayer inventory, TileAdvancedCrafter tileAdvancedCrafter) {

        this.tile = tileAdvancedCrafter;
        // Input
        addSlotToContainer(new Slot(tileAdvancedCrafter, 0, 67, 26));
        addSlotToContainer(new Slot(tileAdvancedCrafter, 1, 85, 26));
        addSlotToContainer(new Slot(tileAdvancedCrafter, 2, 67, 44));
        addSlotToContainer(new Slot(tileAdvancedCrafter, 3, 85, 44));

        // Output
        addSlotToContainer(new SlotFurnace(inventory.player, tileAdvancedCrafter, 4, 143, 34));

        //player inv
        bindPlayerInventory(inventory, 8, 84);
    }

    @Override
    public boolean enchantItem(EntityPlayer player, int i) {
        tile.currentMode = i;
        return false;
    }
}
