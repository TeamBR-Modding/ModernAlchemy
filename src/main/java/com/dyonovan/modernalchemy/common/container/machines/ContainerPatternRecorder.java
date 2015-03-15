package com.dyonovan.modernalchemy.common.container.machines;

import com.dyonovan.modernalchemy.common.container.BaseContainer;
import com.dyonovan.modernalchemy.common.tileentity.machines.TilePatternRecorder;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import openmods.container.ContainerInventoryProvider;

public class ContainerPatternRecorder extends ContainerInventoryProvider<TilePatternRecorder> {

    public ContainerPatternRecorder(InventoryPlayer playerInventory, TilePatternRecorder owner){
        super(playerInventory, owner);

        addSlotToContainer(new Slot(owner.getInventory(), TilePatternRecorder.INPUT_SLOT, 82, 35));
        addSlotToContainer(new Slot(owner.getInventory(), TilePatternRecorder.ITEM_SLOT, 54, 35));
        addSlotToContainer(new SlotFurnace(((InventoryPlayer)playerInventory).player, owner.getInventory(), TilePatternRecorder.OUTPUT_SLOT, 142, 35));
        addPlayerInventorySlots(8, 84);
    }
}
