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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.awt.*;
import java.util.List;

public class TileCompressor extends BaseTile implements IFluidHandler, ITeslaHandler {

    public FluidTank tank;
    private TeslaBank energy;
    private boolean isActive;
    private int currentSpeed;

    public TileCompressor() {
        this.energy = new TeslaBank(1000);
        this.tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
        this.isActive = false;
    }

    public FluidStack setFluidStack(int amount) {
        FluidStack fluidstack = new FluidStack(BlockHandler.fluidCompressedAir, amount);
        return fluidstack;
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
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] {tank.getInfo()};
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

        if(energy.canAcceptEnergy()) {
            chargeFromCoils();
        }

        //if (worldObj.isRemote) return;

        if (energy.getEnergyLevel() > 0 && canFill(tank) && !isPowered()) {
            updateSpeed();
            if (!isActive) isActive = BlockCompressor.toggleIsActive(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            energy.drainEnergy(currentSpeed);
            tank.fill(setFluidStack(100 * currentSpeed), true);

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

    public boolean canFill(FluidTank tank) {
        if(tank.getFluid() == null)
            return true;
        else if(tank.getFluid().amount < tank.getCapacity())
            return true;
        else
            return false;
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

            if(worldObj.isRemote)
                RenderUtils.renderLightningBolt(worldObj, xCoord, yCoord, zCoord, coil, fill);

        }
        while (currentDrain > 0) {
            energy.addEnergy(ConfigHandler.tickTesla);
            currentDrain--;
        }
    }
}
