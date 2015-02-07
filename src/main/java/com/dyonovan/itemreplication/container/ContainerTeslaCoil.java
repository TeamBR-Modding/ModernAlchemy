package com.dyonovan.itemreplication.container;

import com.dyonovan.itemreplication.tileentity.TileTeslaCoil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerTeslaCoil extends Container {

    private TileTeslaCoil tile;

    public ContainerTeslaCoil(TileTeslaCoil tile) {
        super();

        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

}
