package com.dyonovan.modernalchemy.energy;

public interface ITeslaProvider {

    /**
     * Defines how much energy can be pulled from this provider
     * @return An int representing how much can be provided
     */
    public int getEnergyProvided();
}
