package com.dyonovan.modernalchemy.gui;

import com.dyonovan.modernalchemy.container.ContainerFrameEnergy;
import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorCPU;
import com.dyonovan.teambrcore.gui.BaseGui;

public class GuiFrameEnergy extends BaseGui {

    public GuiFrameEnergy(TileReplicatorCPU tileEntity) {
        super(new ContainerFrameEnergy(tileEntity));

    }
}
