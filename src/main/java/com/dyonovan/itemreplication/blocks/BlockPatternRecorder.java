package com.dyonovan.itemreplication.blocks;

import com.dyonovan.itemreplication.tileentity.TEPatternRecorder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Tim on 2/4/2015.
 */
public class BlockPatternRecorder extends BlockContainer {

    protected BlockPatternRecorder(Material material) {
        super(material);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        super.breakBlock(world, x, y, z, block, metadata);
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int eventID, int eventParam) {
        return super.onBlockEventReceived(world, x, y, z, eventID, eventParam);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TEPatternRecorder();
    }
}
