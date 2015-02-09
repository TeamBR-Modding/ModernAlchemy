package com.dyonovan.itemreplication.tileentity;

import com.dyonovan.itemreplication.effects.LightningBolt;
import com.dyonovan.itemreplication.energy.ITeslaHandler;
import com.dyonovan.itemreplication.energy.TeslaBank;
import com.dyonovan.itemreplication.energy.TeslaMachine;
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
    private boolean isRunning;

    TileTeslaMachine() {
        totalProcessingTime = 500;
        timeProcessed = 0;
        currentSpeed = 0;
        isRunning = false;
        energyTank = new TeslaBank(0, 1000);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        energyTank.readFromNBT(tag);
        totalProcessingTime = tag.getInteger("totalProcessingTime");
        timeProcessed = tag.getInteger("timeProcessed");
        currentSpeed = tag.getInteger("currentSpeed");
        isRunning = tag.getBoolean("isRunning");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        energyTank.writeToNBT(tag);
        tag.setInteger("totalProcessingTime", totalProcessingTime);
        tag.setInteger("timeProcessed", timeProcessed);
        tag.setInteger("currentSpeed", currentSpeed);
        tag.setBoolean("isRunning", isRunning);
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

    public boolean IsRunning() {
        return isRunning;
    }

    protected abstract void consumeResources();
    protected abstract void createProducts();
    protected abstract boolean resourcesAvailable();
    protected abstract boolean canOperate();

    private void doWork(){
        // TODO: do we really want pre-consume?
        doWork_preConsume();
    }

    private void doWork_postConsume() {
        // check if resources and energy are available each tick
        // this allows the user to stop the process without losing the item
        if(resourcesAvailable()) {

            // try to do work
            if(energyTank.drainEnergy(currentSpeed + 1)) {
                isRunning = true;
                timeProcessed += currentSpeed;
            }
            else {
                // ran out of energy - stop working but don't reset progress
                isRunning = false;
            }
        }
        else {
            // no resources - stop running and reset progress
            isRunning = false;
            timeProcessed = 0;
        }

        if(timeProcessed >= totalProcessingTime) {
            consumeResources();
            createProducts();
            timeProcessed = 0;
            isRunning = false;
        }
    }

    private void doWork_preConsume() {
        // check if resources are available when we start
        if(timeProcessed <= 0 && resourcesAvailable()) {
            consumeResources();

            // set some progress time so we know to try to do work
            timeProcessed = 1;
        }

        // try to do work
        if(timeProcessed > 0 && energyTank.drainEnergy(currentSpeed + 1)) {
            isRunning = true;
            timeProcessed += currentSpeed;
        }
        else {
            // ran out of energy - stop working but don't reset progress
            isRunning = false;
        }

        if(timeProcessed >= totalProcessingTime) {
            createProducts();
            timeProcessed = 0;
            isRunning = false;
        }
    }

    private void chargeFromCoils() {
        if (energyTank.canAcceptEnergy()) {
            int maxFill = energyTank.getMaxCapacity() - energyTank.getEnergyLevel();
            List<TileTeslaCoil> coils = TeslaMachine.findCoils(worldObj, this);
            int currentDrain = 0;
            for (TileTeslaCoil coil : coils) {
                int fill = coil.getEnergyLevel() > ConfigHandler.tickTesla ? ConfigHandler.tickTesla : coil.getEnergyLevel();
                if (currentDrain + fill > maxFill)
                    fill = maxFill - currentDrain;
                currentDrain += fill;
                coil.drainEnergy(fill);

                if (worldObj.isRemote) {
                    Minecraft.getMinecraft().effectRenderer.addEffect(new LightningBolt(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, coil.xCoord + 0.5, coil.yCoord + 1.5, coil.zCoord + 0.5, fill > 4 ? fill : 4, new Color(255, 255, 255, 255)));
                }
                if(currentDrain >= maxFill)
                    break;
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
