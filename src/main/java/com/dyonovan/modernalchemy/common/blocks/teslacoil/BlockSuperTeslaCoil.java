package com.dyonovan.modernalchemy.common.blocks.teslacoil;

import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileSuperTeslaCoil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSuperTeslaCoil extends BlockTeslaCoil {

    public BlockSuperTeslaCoil() {
        super();
        this.setBlockName(Constants.MODID + ":blockSuperTeslaCoil");
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileSuperTeslaCoil();
    }
}
