package com.dyonovan.modernalchemy.tileentity.machines;

import com.dyonovan.modernalchemy.audio.MachineSound;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.audio.SoundHelper;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.BaseMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.List;

public class TileElectricBellows extends BaseMachine implements IFluidHandler {

    public FluidTank tank;
    private int currentSpeed;
    private int soundCoolDown = 0;

    public TileElectricBellows() {
        this.energyTank = new TeslaBank(1000);
        this.tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
        this.isActive = false;
    }

    /*******************************************************************************************************************
     ****************************************** Compressor Functions ***************************************************
     *******************************************************************************************************************/

    private void updateSpeed() {
        if(energyTank.getEnergyLevel() == 0) {
            currentSpeed = 0;
            return;
        }

        currentSpeed = (energyTank.getEnergyLevel() * 20) / energyTank.getMaxCapacity();
        if(currentSpeed == 0)
            currentSpeed = 1;
    }

    private void compress() {
        if (energyTank.getEnergyLevel() > 0 && canFill(tank) && !isPowered()) {
            if (!isActive) {
                ISound eventHorizonSound = new MachineSound(Constants.MODID + ":compressor", this, 0.1F, 1);
                Minecraft.getMinecraft().getSoundHandler().playSound(eventHorizonSound);
                isActive = true;
            }
            updateSpeed();
            energyTank.drainEnergy(currentSpeed);
            tank.fill(new FluidStack(BlockHandler.fluidCompressedAir, 10 * currentSpeed), true);
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        } else if (isActive)
            isActive = false;
    }

    /*******************************************************************************************************************
     ********************************************* Fluid Functions *****************************************************
     *******************************************************************************************************************/

    public boolean canFill(FluidTank tank) {
        return tank.getFluid() == null || tank.getFluid().amount < tank.getCapacity();
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

    /*******************************************************************************************************************
     ********************************************** Tile Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) {
            /*if(isActive && soundCoolDown <= 0) {
                SoundHelper.playSound("compressor", xCoord, yCoord, zCoord, 0.05F, 0.8F);
                soundCoolDown = 20;
            }
            soundCoolDown--;*/
            return;
        }

        if (energyTank.canAcceptEnergy()) {
            chargeFromCoils();
        }
        compress();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        tank.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tank.writeToNBT(tag);
    }

    /*******************************************************************************************************************
     ********************************************** Misc Functions *****************************************************
     *******************************************************************************************************************/
    @Override
    public void returnWailaHead(List<String> head) {
        head.add(GuiHelper.GuiColor.YELLOW + "Is Compressing: " + GuiHelper.GuiColor.WHITE + (isActive() ? "Yes" : "No"));
        head.add(GuiHelper.GuiColor.YELLOW + "Energy: " + GuiHelper.GuiColor.WHITE + energyTank.getEnergyLevel() + "/" + energyTank.getMaxCapacity() + GuiHelper.GuiColor.TURQUISE + "T");
        head.add(GuiHelper.GuiColor.YELLOW + "Compressed Air: " + GuiHelper.GuiColor.WHITE + tank.getFluidAmount() + "/" + tank.getCapacity() + GuiHelper.GuiColor.TURQUISE + "mb");
    }
}
