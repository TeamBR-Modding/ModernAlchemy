package com.dyonovan.itemreplication.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.dyonovan.itemreplication.energy.ITeslaHandler;
import com.dyonovan.itemreplication.energy.TeslaBank;
import com.dyonovan.itemreplication.handlers.BlockHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileCompressor extends BaseCore implements IFluidHandler, ITeslaHandler {

    public static FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME);
    private TeslaBank energyTesla = new TeslaBank(1000, 1000);

    public TileCompressor() {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        energyTesla.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        energyTesla.writeToNBT(tag);
    }


    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (!resource.isFluidEqual(tank.getFluid()))
        {
            return null;
        }
        tank.setFluid(new FluidStack(BlockHandler.fluidCompressedAir, FluidContainerRegistry.BUCKET_VOLUME));
        return tank.drain(resource.amount, false);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        tank.setFluid(new FluidStack(BlockHandler.fluidCompressedAir, FluidContainerRegistry.BUCKET_VOLUME));
        return tank.drain(maxDrain, false);
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
    public void updateEntity() {
        super.updateEntity();
    }

    @Override
    public boolean isWellFormed() {
        return false;
    }

    @Override
    public void buildStructure() {

    }

    @Override
    public void deconstructStructure() {

    }

    @Override
    public void addEnergy(int maxAmount) {
        energyTesla.addEnergy(maxAmount);
    }

    @Override
    public void drainEnergy(int maxAmount) {
        energyTesla.drainEnergy(maxAmount);
    }

    @Override
    public int getEnergyLevel() {
        return energyTesla.getEnergyLevel();
    }

    @Override
    public TeslaBank getEnergyBank() {
        return energyTesla;
    }
}
