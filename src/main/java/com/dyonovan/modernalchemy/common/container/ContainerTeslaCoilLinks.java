package com.dyonovan.modernalchemy.common.container;

import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileTeslaCoil;

public class ContainerTeslaCoilLinks extends BaseContainer {

    TileTeslaCoil tile;


    public ContainerTeslaCoilLinks(TileTeslaCoil tileEntity) {
        this.tile = tileEntity;
    }
}
