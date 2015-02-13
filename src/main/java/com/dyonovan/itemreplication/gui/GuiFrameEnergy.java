package com.dyonovan.itemreplication.gui;

import com.dyonovan.itemreplication.container.ContainerFrameEnergy;
import com.dyonovan.itemreplication.tileentity.replicator.TileReplicatorCPU;

public class GuiFrameEnergy extends BaseGui {

    public GuiFrameEnergy(TileReplicatorCPU tileEntity) {
        super(new ContainerFrameEnergy(tileEntity));

    }
}
