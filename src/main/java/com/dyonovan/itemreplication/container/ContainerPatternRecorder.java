package com.dyonovan.itemreplication.container;

import com.dyonovan.itemreplication.tileentity.TilePatternRecorder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * Created by Tim on 2/8/2015.
 */
public class ContainerPatternRecorder extends Container {

    private TilePatternRecorder tile;

    public ContainerPatternRecorder(TilePatternRecorder tileEntity){
        tile = tileEntity;

    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
