package com.dyonovan.modernalchemy.common.tileentity.arcfurnace.dummies;

import com.dyonovan.modernalchemy.common.blocks.arcfurnace.dummies.BlockDummyOutputValve;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import openmods.api.IIconProvider;

import java.util.Arrays;
import java.util.HashSet;

public class TileDummyOutputValve extends TileDummy implements IFluidHandler, IIconProvider {

    @Override
    public IIcon getIcon(ForgeDirection rotatedDir) {
        return getCore() != null ? BlockDummyOutputValve.Icons.active : BlockDummyOutputValve.Icons.inActive;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return this.drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (getCore() != null)
            return getCore().outputTank.drain(maxDrain, doDrain);
        else
            return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return getCore() != null && getCore().getOutputTank().getFluidAmount() > 0;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return getCore() != null ? new FluidTankInfo[] { getCore().outputTank.getInfo() } : new FluidTankInfo[0];
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (getCore() != null)
            getCore().outputTank.distributeToSides(50, worldObj, getPosition(), new HashSet(Arrays.asList(ForgeDirection.VALID_DIRECTIONS)));
    }
}
