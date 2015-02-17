package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.machines.TilePatternRecorder;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

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
