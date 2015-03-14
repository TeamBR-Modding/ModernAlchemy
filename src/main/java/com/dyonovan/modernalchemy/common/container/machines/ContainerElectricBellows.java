package com.dyonovan.modernalchemy.common.container.machines;

import com.dyonovan.modernalchemy.common.container.BaseContainer;
import com.dyonovan.modernalchemy.common.tileentity.machines.TileElectricBellows;

public class ContainerElectricBellows extends BaseContainer {

    private TileElectricBellows tile;
    public ContainerElectricBellows(TileElectricBellows tile) {
        this.tile = tile;
    }
}
