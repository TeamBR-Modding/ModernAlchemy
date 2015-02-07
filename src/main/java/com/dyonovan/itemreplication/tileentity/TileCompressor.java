package com.dyonovan.itemreplication.tileentity;

import com.dyonovan.itemreplication.effects.LightningBolt;
import com.dyonovan.itemreplication.energy.ITeslaHandler;
import com.dyonovan.itemreplication.energy.TeslaBank;
import com.dyonovan.itemreplication.handlers.BlockHandler;
import com.dyonovan.itemreplication.handlers.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.awt.*;
import java.util.List;

public class TileCompressor extends BaseTile implements IFluidHandler, ITeslaHandler {

    public static FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME);
    private TeslaBank energyTesla = new TeslaBank(1000);

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

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(energyTesla.canAcceptEnergy()) {
            chargeFromCoils();
        }
    }

    public void chargeFromCoils() {
        int maxFill = energyTesla.getMaxCapacity() - energyTesla.getEnergyLevel();
        List<TileTeslaCoil> coils = findCoils(worldObj, this);
        int currentDrain = 0;
        for(TileTeslaCoil coil : coils) {
            int fill = coil.getEnergyLevel() > ConfigHandler.tickTesla ? ConfigHandler.tickTesla : coil.getEnergyLevel();
            if(currentDrain + fill > maxFill)
                fill = maxFill - currentDrain;
            currentDrain += fill;
            coil.drainEnergy(fill);

            if(worldObj.isRemote)
                Minecraft.getMinecraft().effectRenderer.addEffect(new LightningBolt(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, coil.xCoord + 0.5, coil.yCoord + 0.5, coil.zCoord + 0.5, fill > 4 ? fill : 4, new Color(255, 255, 255, 255)));
        }
        while(currentDrain > 0) {
            energyTesla.addEnergy(ConfigHandler.tickTesla);
            currentDrain--;
        }
    }
}
