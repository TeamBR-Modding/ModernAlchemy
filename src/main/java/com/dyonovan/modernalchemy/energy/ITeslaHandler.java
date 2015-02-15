package com.dyonovan.modernalchemy.energy;

public interface ITeslaHandler {

    /**
     * Add energy to your bank
     * @param maxAmount
     */
    public void addEnergy(int maxAmount);

    /**
     * Drain energy from tank
     * @param maxAmount
     * @return energy drained
     */
    public int drainEnergy(int maxAmount);

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
