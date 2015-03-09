package com.dyonovan.modernalchemy.tileentity.arcfurnace.dummies;

import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.TileArcFurnaceCore;
import com.dyonovan.modernalchemy.tileentity.misc.TileTank;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileDummyOutputValve extends TileDummy implements IFluidHandler {

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
        TileArcFurnaceCore core = (TileArcFurnaceCore)getCore();
        if(core != null)
            return core.drain(ForgeDirection.SOUTH, maxDrain, doDrain);
        else
            return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        TileArcFurnaceCore core = (TileArcFurnaceCore)getCore();
        return core != null && core.getOutputTank().getFluidAmount() > 0;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[0];
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(getCore() != null) {
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                if (getTileInDirection(dir) != null && getTileInDirection(dir) instanceof TileTank) {
                    TileTank tank = (TileTank) getTileInDirection(dir);
                    if (((TileArcFurnaceCore)getCore()).getOutputTank().getFluid() != null && tank.tank.getFluidAmount() + 100 < tank.tank.getCapacity()) {
                        boolean flag = tank.tank.getFluid() == null || tank.tank.getFluid().getFluid() == BlockHandler.fluidActinium;
                        if(flag) {
                            tank.fill(dir.getOpposite(), new FluidStack(BlockHandler.fluidActinium, 100), true);
                            ((TileArcFurnaceCore) getCore()).getOutputTank().drain(100, true);
                        }
                    }
                }
            }
        }
    }
}
