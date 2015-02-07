package com.dyonovan.itemreplication.blocks;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockOreActinium extends Block {

    public BlockOreActinium() {
        super(Material.rock);
        this.setBlockName(Constants.MODID + ":blockOreActinium");
        this.setCreativeTab(ItemReplication.tabItemReplication);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon(Constants.MODID + ":actinium");
    }

}
