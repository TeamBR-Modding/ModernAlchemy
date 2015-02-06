package com.dyonovan.itemreplication.blocks;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.TETeslaBase;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTeslaBase extends BlockContainer {


    public BlockTeslaBase() {
        super(Material.iron);
        this.setHardness(1.5F);
        this.setBlockName(Constants.MODID + ":blockTeslaBase");
        this.setCreativeTab(ItemReplication.tabItemReplication);
        this.setBlockBounds(0F, 0F, 0F, 1F, 3F,1F);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }


    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TETeslaBase();
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
}
