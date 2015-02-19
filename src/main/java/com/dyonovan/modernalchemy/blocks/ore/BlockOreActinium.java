package com.dyonovan.modernalchemy.blocks.ore;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.util.WorldUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class BlockOreActinium extends Block {

    public BlockOreActinium() {
        super(Material.rock);
        this.setBlockName(Constants.MODID + ":blockOreActinium");
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon(Constants.MODID + ":actinium");
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
    {
        super.breakBlock(world, x, y, z, par5, par6);
        WorldUtils.expelItem(world, x, y, z, new ItemStack(ItemHandler.itemActinium));
    }
}
