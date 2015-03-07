package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.teambrcore.container.BaseContainer;

public class ContainerTeslaCoilLinks extends BaseContainer {

    TileTeslaCoil tile;


    public ContainerTeslaCoilLinks(TileTeslaCoil tileEntity) {
        this.tile = tileEntity;
    }
}
