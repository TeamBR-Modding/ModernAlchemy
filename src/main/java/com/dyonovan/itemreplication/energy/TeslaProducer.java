package com.dyonovan.itemreplication.energy;

import cofh.api.energy.EnergyStorage;
import com.dyonovan.itemreplication.handlers.ConfigHandler;
import net.minecraft.world.World;

/**
 * Created by Tim on 2/9/2015.
 */
public class TeslaProducer {

    private TeslaBank tankTesla;
    private EnergyStorage tankRF;

    public TeslaProducer(TeslaBank tankTesla, EnergyStorage tankRF){
        this.tankTesla = tankTesla;
        this.tankRF = tankRF;
    }

    public boolean produceTesla(){
        boolean updated = false;
        // get the max amount of Tesla we can generate
        int energyTesla = Math.min(ConfigHandler.maxCoilGenerate, tankRF.getEnergyStored() / ConfigHandler.rfPerTesla);

        // get the max amount of Tesla we can accept
        energyTesla = Math.min(energyTesla, tankTesla.getMaxCapacity() - tankTesla.getEnergyLevel());

        if(energyTesla > 0) {
            int energyRF = energyTesla * ConfigHandler.rfPerTesla;
            tankRF.setEnergyStored(tankRF.getEnergyStored() - energyRF);
            tankTesla.addEnergy(energyTesla);
            updated = true;
        }

        return updated;
    }
}
