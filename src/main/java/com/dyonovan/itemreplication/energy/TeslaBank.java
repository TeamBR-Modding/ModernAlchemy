package com.dyonovan.itemreplication.energy;

import net.minecraft.nbt.NBTTagCompound;

public class TeslaBank {

    private int currentLevel;
    private int maxCapacity;

    public TeslaBank() {
        this(0, 1000);
    }

    public TeslaBank(int maxLevel) {
        this(0, maxLevel);
    }

    public TeslaBank(int initialLevel, int maxLevel) {
        currentLevel = initialLevel;
        maxCapacity = maxLevel;
    }

    public void setEnergyLevel(int level) {
        currentLevel = level;
    }

    public int getEnergyLevel() {
        return currentLevel;
    }

    public void drainEnergy(int amount) {
        if(currentLevel > amount) {
            currentLevel -= amount;
        }
        else
            currentLevel = 0;
    }

    public void addEnergy(int amount) {
        if(currentLevel + amount <= maxCapacity)
            currentLevel += amount;
        else
            currentLevel = maxCapacity;
    }

    public void setMaxCapacity(int max) {
        maxCapacity = max;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger("currentTeslaLevel", currentLevel);
        tag.setInteger("maxTeslaCapacity", maxCapacity);
    }

    public void readFromNBT(NBTTagCompound tag) {
        currentLevel = tag.getInteger("currentTeslaLevel");
        maxCapacity = tag.getInteger("maxTeslaCapacity");
    }
}
