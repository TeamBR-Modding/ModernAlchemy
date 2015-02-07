package com.dyonovan.itemreplication.blocks;

import com.dyonovan.itemreplication.lib.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBlank extends Block {

    public BlockBlank() {
        super(Material.plants);
        this.setBlockName(Constants.MODID + ":blockBlank");
        this.setBlockBounds(0F, 0F, 0F, 0F, 0F, 0F);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    @Override
    public Item getItemDropped(int par1, Random par2Random, int par3)
    {
        return null;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon(Constants.MODID + ":blank");
    }


}
