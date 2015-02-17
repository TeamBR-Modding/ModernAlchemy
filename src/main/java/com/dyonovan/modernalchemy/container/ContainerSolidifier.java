package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.tileentity.machines.TileSolidifier;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ContainerSolidifier extends BaseContainer {

    public ContainerSolidifier(InventoryPlayer inventory, TileSolidifier tile) {
        addSlotToContainer(new SlotFurnace(inventory.player, tile, 0, 146, 34));
        bindPlayerInventory(inventory, 8, 84);
        setCanSendToTile(false);
    }
}
