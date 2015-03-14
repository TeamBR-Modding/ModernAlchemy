package com.dyonovan.modernalchemy.common.container.machines;

import com.dyonovan.modernalchemy.common.container.BaseContainer;
import com.dyonovan.modernalchemy.common.tileentity.machines.TilePatternRecorder;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

public class ContainerPatternRecorder extends BaseContainer {

    private TilePatternRecorder tile;

    public ContainerPatternRecorder(InventoryPlayer playerInventory, TilePatternRecorder tileEntity){
        tile = tileEntity;

        addSlotToContainer(new Slot(tile, TilePatternRecorder.INPUT_SLOT, 82, 35));
        addSlotToContainer(new Slot(tile, TilePatternRecorder.ITEM_SLOT, 44, 35));
        addSlotToContainer(new SlotFurnace(playerInventory.player, tile, TilePatternRecorder.OUTPUT_SLOT, 142, 35));
        bindPlayerInventory(playerInventory, 8, 84);
    }
}
