package com.dyonovan.itemreplication.container;

import com.dyonovan.itemreplication.tileentity.TileCompressor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerCompressor extends Container {

    private TileCompressor tile;

    public ContainerCompressor(TileCompressor tile) {
        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }
}
