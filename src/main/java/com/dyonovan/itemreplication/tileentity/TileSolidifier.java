package com.dyonovan.itemreplication.tileentity;

import com.dyonovan.itemreplication.blocks.BlockCompressor;
import com.dyonovan.itemreplication.effects.LightningBolt;
import com.dyonovan.itemreplication.energy.ITeslaHandler;
import com.dyonovan.itemreplication.energy.TeslaBank;
import com.dyonovan.itemreplication.energy.TeslaMachine;
import com.dyonovan.itemreplication.handlers.BlockHandler;
import com.dyonovan.itemreplication.handlers.ConfigHandler;
import com.dyonovan.itemreplication.helpers.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.awt.*;
import java.util.List;

public class TileSolidifier extends BaseTile implements IFluidHandler, ITeslaHandler, ISidedInventory {

    public FluidTank tank;
    private TeslaBank energy;
    private boolean isActive;
    private int currentSpeed;
    public ItemStack inventory[];
    private static final int OUTPUT_SLOT = 0;

    public TileSolidifier() {
        tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
        tank.setFluid(new FluidStack(BlockHandler.fluidActinium, 0));
        this.energy = new TeslaBank(1000);
        this.isActive = false;
        inventory = new ItemStack[1];
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        energy.readFromNBT(tag);
        tank.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        energy.writeToNBT(tag);
        tank.writeToNBT(tag);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource.getFluid() != BlockHandler.fluidActinium) return 0;
        return tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (!resource.isFluidEqual(tank.getFluid()))
        {
            return null;
        }
        return tank.drain(resource.amount, true);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { tank.getInfo() };
    }

    @Override
    public void addEnergy(int maxAmount) {
        energy.addEnergy(maxAmount);
    }

    @Override
    public void drainEnergy(int maxAmount) {
        energy.drainEnergy(maxAmount);
    }

    @Override
    public int getEnergyLevel() {
        return energy.getEnergyLevel();
    }

    @Override
    public TeslaBank getEnergyBank() {
        return energy;
    }

    public void setEnergy(int amount) {
        energy.setEnergyLevel(amount);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (energy.canAcceptEnergy()) {
            chargeFromCoils();
        }
        //if (worldObj.isRemote) return;

        if (energy.getEnergyLevel() > 0 && tank.getFluid() != null && tank.getFluidAmount() > 0 && !isPowered()) {
            updateSpeed();
            if (!isActive) isActive = BlockCompressor.toggleIsActive(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            //TODO ACTUAL PROCESSING
            super.markDirty();
        } else if (isActive) isActive = BlockCompressor.toggleIsActive(this.worldObj, this.xCoord, this.yCoord, this.zCoord);


    }

    public void updateSpeed() {
        if(energy.getEnergyLevel() == 0) {
            currentSpeed = 0;
            return;
        }

        currentSpeed = (energy.getEnergyLevel() * 20) / energy.getMaxCapacity();
        if(currentSpeed == 0)
            currentSpeed = 1;
    }

    public void chargeFromCoils() {
        int maxFill = energy.getMaxCapacity() - energy.getEnergyLevel();
        List<TileTeslaCoil> coils = TeslaMachine.findCoils(worldObj, this);
        int currentDrain = 0;
        for (TileTeslaCoil coil : coils) {
            if (coil.getEnergyLevel() <= 0) continue;
            int fill = coil.getEnergyLevel() > ConfigHandler.tickTesla ? ConfigHandler.tickTesla : coil.getEnergyLevel();
            if (currentDrain + fill > maxFill)
                fill = maxFill - currentDrain;
            currentDrain += fill;
            coil.drainEnergy(fill);

            if (worldObj.isRemote)
                RenderUtils.renderLightningBolt(worldObj, xCoord, yCoord, zCoord, coil, fill);
        }
        while (currentDrain > 0) {
            energy.addEnergy(ConfigHandler.tickTesla);
            currentDrain--;
        }
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
        return false;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {

    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return false;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return false;
    }
}
