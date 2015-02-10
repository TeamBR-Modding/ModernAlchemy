package com.dyonovan.itemreplication.energy;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Tim on 2/9/2015.
 */
public class TeslaConsumer {
    private TeslaBank tank;
    private int teslaPerTick;  // base Tesla/tick consumed
    private float remainderEnergy; // should always be < 1

    public TeslaConsumer(TeslaBank tank, int teslaPerTick){
        this.tank = tank;
        this.teslaPerTick = teslaPerTick;  // energy consumption at full speed
        this.remainderEnergy = 0.0f;
    }

    public void readFromNBT(NBTTagCompound tag) {
        teslaPerTick = tag.getInteger("teslaPerTick");
        remainderEnergy = tag.getFloat("remainderEnergy");
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger("teslaPerTick", teslaPerTick);
        tag.setFloat("remainderEnergy", remainderEnergy);
    }

    /**
     * Consume one tick of energy. Call during update when machine is working.
     * Recommend aggregating the return value until work is finished.
     * @return Work speed % as integer 0-100. Will return -1 if not enough energy.
     */
    public int consumeEnergy(){

        // The % fill of the tank is the % speed.  TODO: should we scale it differently?
        float speed = (float)tank.getEnergyLevel() / (float)tank.getMaxCapacity();

        // add one tick's worth of energy to the remainder
        remainderEnergy += teslaPerTick * speed;

        // floor the value
        int energyRequest = (int)(remainderEnergy);

        // then subtract whatever we are going to request.
        // we don't care if we actually get this energy -
        // we just want to request extra once in an while
        remainderEnergy -= energyRequest;

        int actualEnergy = tank.drainEnergy(energyRequest);

        if(actualEnergy <= 0 && energyRequest > 0)
            return -1; // we got no energy, so we failed.
        else
            return (int)(speed * 100.0f);
    }

    public static int secondsToSpeed(int seconds){
        // max speed is 100/tick
        // 20 ticks/sec
        return seconds * 2000;
    }

}
