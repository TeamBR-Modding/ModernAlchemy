package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.machines.TileCompressor;

public class ContainerCompressor extends BaseContainer {

    private TileCompressor tile;
    public ContainerCompressor(TileCompressor tile) {
        this.tile = tile;
    }
}
