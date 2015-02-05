package com.dyonovan.itemreplication.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class BlastFurnaceContainer extends Container {
    public BlastFurnaceContainer() {

    }
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
