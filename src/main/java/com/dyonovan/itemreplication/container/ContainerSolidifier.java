package com.dyonovan.itemreplication.container;

import com.dyonovan.itemreplication.tileentity.TileSolidifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerSolidifier extends Container {
    public ContainerSolidifier(TileSolidifier tile) {
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
