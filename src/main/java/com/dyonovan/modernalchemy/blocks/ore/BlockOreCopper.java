package com.dyonovan.modernalchemy.blocks.ore;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockOreCopper extends Block {

    public BlockOreCopper() {
        super(Material.rock);
        this.setBlockName(Constants.MODID + ":blockOreCopper");
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
        this.setHardness(2.0F);
        this.setHarvestLevel("pickaxe", 1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon(Constants.MODID + ":copper_ore");
    }
}
