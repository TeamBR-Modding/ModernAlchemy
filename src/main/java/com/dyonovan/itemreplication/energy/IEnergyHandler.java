package com.dyonovan.itemreplication.energy;

public interface IEnergyHandler {

    /**
     * Add energy to your bank
     * @param maxAmount
     */
    public void addEnergy(int maxAmount);

    /**
     * Drain energy from tank
     * @param maxAmount
     */
    public void drainEnergy(int maxAmount);

    /**
     * Get the tanks energy level
     * @return tank energy level
     */
    public int getEnergyLevel();

    /**
     * Get the TeslaBank for tile
     * @return tank object
     */
    public TeslaBank getEnergyBank();
}
