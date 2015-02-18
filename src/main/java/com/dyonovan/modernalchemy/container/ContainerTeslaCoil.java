package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;

public class ContainerTeslaCoil extends BaseContainer {

    private TileTeslaCoil tile;
    //private int lastRFPower = 0;
    //private int lastTeslaPower = 0;

    public ContainerTeslaCoil(TileTeslaCoil tileentity) {
        //super();

        this.tile = tileentity;
    }

    /*@Override
    public void addCraftingToCrafters(ICrafting crafter) {
        super.addCraftingToCrafters(crafter);
        crafter.sendProgressBarUpdate(this, 0, this.tile.getRFEnergyStored());
        crafter.sendProgressBarUpdate(this, 1, this.tile.getEnergyLevel());
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);

            if (this.lastRFPower != this.tile.getRFEnergyStored())
                icrafting.sendProgressBarUpdate(this, 0, this.tile.getRFEnergyStored());
            if (this.lastTeslaPower != this.tile.getEnergyLevel())
                icrafting.sendProgressBarUpdate(this, 1, this.tile.getEnergyLevel());
        }

        this.lastRFPower = this.tile.getRFEnergyStored();
        this.lastTeslaPower = this.tile.getEnergyLevel();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int i, int j) {
        switch (i) {
            case 0:
                this.tile.setRFEnergyStored(j);
                break;
            case 1:
                this.tile.setTeslaEnergyStored(j);
                break;
        }
    }*/

}
