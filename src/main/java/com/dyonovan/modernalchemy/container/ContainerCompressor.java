package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.tileentity.machines.TileCompressor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraftforge.fluids.FluidStack;

public class ContainerCompressor extends Container {

    private TileCompressor tile;
    private int lastPower, lastTank;

    public ContainerCompressor(TileCompressor tile) {
        this.tile = tile;
        this.lastPower = 0;
        this.lastTank = 0;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafter) {
        super.addCraftingToCrafters(crafter);
        crafter.sendProgressBarUpdate(this, 0, this.tile.getEnergyLevel());
        crafter.sendProgressBarUpdate(this, 1, this.tile.tank.getFluidAmount());
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);

            if (this.lastPower != this.tile.getEnergyLevel())
                icrafting.sendProgressBarUpdate(this, 0, this.tile.getEnergyLevel());
            if (this.lastTank != this.tile.tank.getFluidAmount())
                icrafting.sendProgressBarUpdate(this, 1, this.tile.tank.getFluidAmount());
        }

        this.lastPower = this.tile.getEnergyLevel();
        this.lastTank = this.tile.tank.getFluidAmount();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int i, int j) {
        switch (i) {
            case 0:
                this.tile.setEnergy(j);
                break;
            case 1:
                this.tile.tank.setFluid(new FluidStack(BlockHandler.fluidCompressedAir, j));
                break;
        }
    }
}
