package com.dyonovan.itemreplication.blocks;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.handlers.BlockHandler;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.TETeslaBase;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTeslaBase extends BlockContainer {


    public BlockTeslaBase() {
        super(Material.iron);
        this.setHardness(1.5F);
        this.setBlockName(Constants.MODID + ":blockTeslaBase");
        this.setCreativeTab(ItemReplication.tabItemReplication);
        this.setBlockBounds(0F, 0F, 0F, 1F, 3F, 1F);
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

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y + 1, z) && super.canPlaceBlockAt(world, x, y + 2, z);
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int side, EntityPlayer player) {
        world.setBlock(x, y + 1, z, Blocks.air);
        world.setBlock(x, y + 2, z, Blocks.air);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack) {
        //if (!world.isRemote) {
            world.setBlock(x, y + 1, z, BlockHandler.blockBlank);
            world.setBlock(x, y + 2, z, BlockHandler.blockBlank);
        //}
    }

}
