package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorCPU;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerReplicatorCpu extends BaseContainer {

    private TileReplicatorCPU tile;

    public ContainerReplicatorCpu(InventoryPlayer playerInventory, TileReplicatorCPU tileEntity) {
        tile = tileEntity;

        addSlotToContainer(new Slot(tile, 0, 44, 35));
        addSlotToContainer(new Slot(tile, 1, 82, 35));
        addSlotToContainer(new SlotFurnace(playerInventory.player, tile, 2, 142, 35));
        bindPlayerInventory(playerInventory, 8, 84);
    }

}
