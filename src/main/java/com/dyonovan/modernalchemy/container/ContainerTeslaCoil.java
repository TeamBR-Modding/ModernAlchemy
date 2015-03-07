package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.teambrcore.container.BaseContainer;

public class ContainerTeslaCoil extends BaseContainer {

    private TileTeslaCoil tile;
    public ContainerTeslaCoil(TileTeslaCoil tileentity) {
        this.tile = tileentity;
    }
}
