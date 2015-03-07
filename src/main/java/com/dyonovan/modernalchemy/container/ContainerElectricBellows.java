package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.machines.TileElectricBellows;
import com.dyonovan.teambrcore.container.BaseContainer;

public class ContainerElectricBellows extends BaseContainer {

    private TileElectricBellows tile;
    public ContainerElectricBellows(TileElectricBellows tile) {
        this.tile = tile;
    }
}
