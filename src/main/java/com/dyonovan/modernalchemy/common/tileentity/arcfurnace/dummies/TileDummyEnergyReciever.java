package com.dyonovan.modernalchemy.common.tileentity.arcfurnace.dummies;

import com.dyonovan.modernalchemy.energy.ITeslaHandler;
import com.dyonovan.modernalchemy.energy.TeslaBank;

public class TileDummyEnergyReciever extends TileDummy implements ITeslaHandler {

    @Override
    public void updateEntity() {
        if(worldObj.isRemote) return;
        if(getCore() != null) {
            if(getCore().getEnergyBank().canAcceptEnergy())
                chargeFromCoils(getEnergyBank());
        }
    }

    @Override
    public void addEnergy(int maxAmount) {
        if(getCore() != null) {
            getCore().addEnergy(maxAmount);
            updateCore();
        }
    }

    @Override
    public int drainEnergy(int maxAmount) {
        if(getCore() != null) {
            int drain = getCore().drainEnergy(maxAmount);
            updateCore();
            return drain;
        }
        return 0;
    }

    @Override
    public int getEnergyLevel() {
        if(getCore() != null) {
            return getCore().getEnergyLevel();
        }
        return 0;
    }

    @Override
    public TeslaBank getEnergyBank() {
        if(getCore() != null) {
            updateCore();
            return getCore().getEnergyBank();
        }
        return null;
    }
}
