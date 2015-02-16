package com.dyonovan.modernalchemy.tileentity.machines;

import com.dyonovan.modernalchemy.blocks.machines.BlockCompressor;
import com.dyonovan.modernalchemy.energy.ITeslaHandler;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.tileentity.BaseTile;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.util.RenderUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.List;

public class TileCompressor extends BaseTile implements IFluidHandler, ITeslaHandler {

    public FluidTank tank;
    private TeslaBank energy;
    private int currentSpeed;

    public TileCompressor() {
        this.energy = new TeslaBank(1000);
        this.tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
        this.isActive = false;
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
        return tank.drain(resource.amount, doDrain);
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
    public int drainEnergy(int maxAmount) {
        return energy.drainEnergy(maxAmount);
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

        if (worldObj.isRemote) return;

        if(energy.canAcceptEnergy()) {
            chargeFromCoils();
        }

        if (!isPowered()) {
            if (energy.getEnergyLevel() > 0 && canFill(tank) && !isPowered()) {
                if (!isActive)
                    isActive = BlockCompressor.toggleIsActive(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                updateSpeed();
                energy.drainEnergy(currentSpeed);
                tank.fill(new FluidStack(BlockHandler.fluidCompressedAir, 10 * currentSpeed), true);
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        } else if (isActive)
            isActive = BlockCompressor.toggleIsActive(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
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
        return tank.getFluid() == null || tank.getFluid().amount < tank.getCapacity();
    }

    public void chargeFromCoils() {
        int maxFill = energy.getMaxCapacity() - energy.getEnergyLevel();
        List<TileTeslaCoil> coils = findCoils(worldObj, this);
        int currentDrain = 0;
        for (TileTeslaCoil coil : coils) {
            if (coil.getEnergyLevel() <= 0) continue;
            int fill = coil.getEnergyLevel() > ConfigHandler.maxCoilTransfer ? ConfigHandler.maxCoilTransfer : coil.getEnergyLevel();
            if (currentDrain + fill > maxFill)
                fill = maxFill - currentDrain;
            currentDrain += fill;
            coil.drainEnergy(fill);

            RenderUtils.sendBoltToClient(xCoord, yCoord, zCoord, coil, fill);

        }
        while (currentDrain > 0) {
            energy.addEnergy(ConfigHandler.maxCoilTransfer);
            currentDrain--;
        }
    }
}
