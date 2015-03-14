package com.dyonovan.modernalchemy.common.container;

import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileTeslaCoil;

public class ContainerTeslaCoil extends BaseContainer {

    private TileTeslaCoil tile;
    public ContainerTeslaCoil(TileTeslaCoil tileentity) {
        this.tile = tileentity;
    }
}
