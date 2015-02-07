package com.dyonovan.itemreplication.blocks;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.TileTeslaStand;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTeslaStand extends BlockContainer {

    public BlockTeslaStand() {
        super(Material.plants);
        this.setBlockName(Constants.MODID + ":blockTeslaStand");
        this.setHardness(1.5F);
        this.setCreativeTab(ItemReplication.tabItemReplication);
        this.setBlockBounds(0.36875F, 0F, 0.36875F, 0.65625F, 1F, 0.65625F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileTeslaStand();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return true;
    }
}
