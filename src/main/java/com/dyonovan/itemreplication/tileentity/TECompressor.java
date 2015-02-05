package com.dyonovan.itemreplication.tileentity;

import com.dyonovan.itemreplication.handlers.BlockHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TECompressor extends BaseTile implements IFluidHandler {

    protected FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME);

    public TECompressor() {

        //tank.setFluid(new FluidStack(BlockHandler.fluidActinium, FluidContainerRegistry.BUCKET_VOLUME));
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
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
        tank.setFluid(new FluidStack(BlockHandler.fluidActinium, FluidContainerRegistry.BUCKET_VOLUME));
        return tank.drain(resource.amount, false);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        tank.setFluid(new FluidStack(BlockHandler.fluidActinium, FluidContainerRegistry.BUCKET_VOLUME));
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
}
