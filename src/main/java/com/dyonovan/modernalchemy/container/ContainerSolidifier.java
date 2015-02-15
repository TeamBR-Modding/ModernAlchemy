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
import net.minecraftforge.fluids.FluidStack;

public class ContainerSolidifier extends Container {

    private TileSolidifier tile;
    private int lastPower, lastTank, timeProcessed;
    private InventoryPlayer inventory;

    public ContainerSolidifier(InventoryPlayer inventory, TileSolidifier tile) {
        this.tile = tile;
        this.inventory = inventory;
        this.lastPower = 0;

        addSlotToContainer(new SlotFurnace(inventory.player, tile, 0, 146, 34));
        bindPlayerInventory(inventory);

    }

    private void bindPlayerInventory(InventoryPlayer playerInventory)
    {
        // Inventory
        for(int y = 0; y < 3; y++)
            for(int x = 0; x < 9; x++)
                addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));

        // Action Bar
        for(int x = 0; x < 9; x++)
            addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 142));
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafter) {
        super.addCraftingToCrafters(crafter);
        crafter.sendProgressBarUpdate(this, 0, this.tile.getEnergyLevel());
        crafter.sendProgressBarUpdate(this, 1, this.tile.tank.getFluid().amount);
        crafter.sendProgressBarUpdate(this, 2, this.tile.timeProcessed);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (Object crafter : this.crafters) {
            ICrafting icrafting = (ICrafting) crafter;
            if (this.lastPower != this.tile.getEnergyLevel())
                icrafting.sendProgressBarUpdate(this, 0, this.tile.getEnergyLevel());
            if (this.lastTank != this.tile.tank.getFluidAmount())
                icrafting.sendProgressBarUpdate(this, 1, this.tile.tank.getFluidAmount());
            if (this.timeProcessed != this.tile.timeProcessed)
                icrafting.sendProgressBarUpdate(this, 2, this.timeProcessed);
        }

        this.lastPower = this.tile.getEnergyLevel();
        this.lastTank = this.tile.tank.getFluidAmount();
        this.timeProcessed = this.tile.timeProcessed;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int i, int j) {
        switch (i) {
            case 0:
                this.tile.setEnergy(j);
                break;
            case 1:
                this.tile.tank.setFluid(new FluidStack(BlockHandler.fluidActinium, j));
                break;
            case 2:
                this.tile.timeProcessed = j;
                break;
        }
    }

}
