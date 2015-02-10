package com.dyonovan.itemreplication.energy;

/**
 * Interface for Tesla transmitters.
 * Must be implemented on a TileEntity, or else bad casts!
 */
public interface ITeslaTransmitter {

    /**
     * Transmit energy to a receiver.
     * @param maxAmount - requested amount
     * @return actual amount
     */
    public int transmitEnergy(int maxAmount);
}
