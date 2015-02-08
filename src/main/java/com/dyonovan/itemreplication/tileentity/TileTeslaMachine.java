package com.dyonovan.itemreplication.tileentity;

import com.dyonovan.itemreplication.effects.LightningBolt;
import com.dyonovan.itemreplication.energy.ITeslaHandler;
import com.dyonovan.itemreplication.energy.TeslaBank;
import com.dyonovan.itemreplication.handlers.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

import java.awt.*;
import java.util.List;

/**
 * Created by Tim on 2/8/2015.
 */
public abstract class TileTeslaMachine extends BaseTile implements ITeslaHandler {

    protected int totalProcessingTime, timeProcessed, currentSpeed;

    private TeslaBank energyTank;

    TileTeslaMachine() {
        totalProcessingTime = 500;
        timeProcessed = 0;
        currentSpeed = 0;
        energyTank = new TeslaBank(0, 1000);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        energyTank.readFromNBT(tag);
        totalProcessingTime = tag.getInteger("totalProcessingTime");
        timeProcessed = tag.getInteger("timeProcessed");
        currentSpeed = tag.getInteger("currentSpeed");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        energyTank.writeToNBT(tag);
        tag.setInteger("totalProcessingTime", totalProcessingTime);
        tag.setInteger("timeProcessed", timeProcessed);
        tag.setInteger("currentSpeed", currentSpeed);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(canOperate()) {
            // work first to drain energy
            doWork();
            // then accept energy so our tank can stay "full"
            chargeFromCoils();
            // update speed after draining/charging
            updateSpeed();
        }
    }

    protected abstract void consumeResources();
    protected abstract void createProducts();
    protected abstract boolean canStartWork();
    protected abstract boolean canOperate();

    private void doWork() {
        if(timeProcessed <= 0 && canStartWork() && energyTank.drainEnergy(currentSpeed + 1)) {
            consumeResources();
            timeProcessed += currentSpeed;
        }
        else if(timeProcessed > 0 && energyTank.drainEnergy(currentSpeed + 1))
            timeProcessed += currentSpeed;

        if(timeProcessed >= totalProcessingTime) {
            createProducts();
            timeProcessed = 0;
        }
    }

    private void chargeFromCoils() {
        if (energyTank.canAcceptEnergy()) {
            int maxFill = energyTank.getMaxCapacity() - energyTank.getEnergyLevel();
            List<TileTeslaCoil> coils = findCoils(worldObj, this);
            int currentDrain = 0;
            for (TileTeslaCoil coil : coils) {
                int fill = coil.getEnergyLevel() > ConfigHandler.tickTesla ? ConfigHandler.tickTesla : coil.getEnergyLevel();
                if (currentDrain + fill > maxFill)
                    fill = maxFill - currentDrain;
                currentDrain += fill;
                coil.drainEnergy(fill);

                if (worldObj.isRemote) {
                    Minecraft.getMinecraft().effectRenderer.addEffect(new LightningBolt(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, coil.xCoord + 0.5, coil.yCoord + 0.5, coil.zCoord + 0.5, fill > 4 ? fill : 4, new Color(255, 255, 255, 255)));
                }
            }
            while (currentDrain > 0) {
                energyTank.addEnergy(1);
                currentDrain--;
            }
        }
    }

    private void updateSpeed() {
        if(energyTank.getEnergyLevel() == 0) {
            currentSpeed = 0;
        }
        else {
            currentSpeed = (energyTank.getEnergyLevel() * 20) / energyTank.getMaxCapacity();
            if (currentSpeed == 0)
                currentSpeed = 1;
        }
    }

    @Override
    public void addEnergy(int maxAmount) { energyTank.addEnergy(maxAmount); }

    @Override
    public void drainEnergy(int maxAmount) { energyTank.drainEnergy(maxAmount); }

    @Override
    public int getEnergyLevel() { return energyTank.getEnergyLevel(); }

    @Override
    public TeslaBank getEnergyBank() { return energyTank; }
}
