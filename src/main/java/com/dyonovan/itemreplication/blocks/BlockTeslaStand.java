package com.dyonovan.itemreplication.blocks;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.TileTeslaStand;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTeslaStand extends BlockBase {

    public BlockTeslaStand() {
        super(Material.iron);
        this.setBlockName(Constants.MODID + ":blockTeslaStand");
        this.setHardness(1.5F);
        this.setCreativeTab(ItemReplication.tabItemReplication);
        this.setBlockBounds(0.34375F, 0F, 0.34375F, 0.65625F, 1F, 0.65625F);
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
        return world.getBlock(x, y - 1, z) instanceof BlockTeslaBase || world.getBlock(x, y - 1, z) instanceof BlockTeslaStand;
    }


    //TODO have stand/coil break everything above
}
