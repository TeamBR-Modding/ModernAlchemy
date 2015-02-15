package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorCPU;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerFrameEnergy extends Container {

    public ContainerFrameEnergy(TileReplicatorCPU tileEntity) {
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }
}
