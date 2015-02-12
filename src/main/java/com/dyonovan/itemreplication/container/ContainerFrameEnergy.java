package com.dyonovan.itemreplication.container;

import com.dyonovan.itemreplication.tileentity.TileFrameEnergy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerFrameEnergy extends Container {

    public ContainerFrameEnergy(TileFrameEnergy tileEntity) {
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }
}
