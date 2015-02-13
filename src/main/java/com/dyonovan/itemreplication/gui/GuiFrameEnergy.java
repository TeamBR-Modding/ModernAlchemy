package com.dyonovan.itemreplication.gui;

import com.dyonovan.itemreplication.container.ContainerFrameEnergy;
import com.dyonovan.itemreplication.tileentity.replicator.ReplicatorCPU;

public class GuiFrameEnergy extends BaseGui {

    public GuiFrameEnergy(ReplicatorCPU tileEntity) {
        super(new ContainerFrameEnergy(tileEntity));

    }
}
