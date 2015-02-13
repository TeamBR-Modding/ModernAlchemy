package com.dyonovan.itemreplication.container;

import com.dyonovan.itemreplication.tileentity.replicator.ReplicatorCPU;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerFrameEnergy extends Container {

    public ContainerFrameEnergy(ReplicatorCPU tileEntity) {
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }
}
