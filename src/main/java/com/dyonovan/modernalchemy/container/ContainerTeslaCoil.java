package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;

public class ContainerTeslaCoil extends BaseContainer {

    private TileTeslaCoil tile;
    public ContainerTeslaCoil(TileTeslaCoil tileentity) {
        this.tile = tileentity;
    }
}
