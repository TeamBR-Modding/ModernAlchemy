package com.dyonovan.itemreplication.blocks;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tiles.BaseTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDummy extends BlockContainer {

    public BlockDummy(String name) {
        super(Material.rock);
        this.setBlockName(Constants.MODID + ":" + name);
        this.setCreativeTab(ItemReplication.tabItemReplication);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int par2) {
        return new BaseTile();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon(Constants.MODID + ":blastFurnace");
    }
}
