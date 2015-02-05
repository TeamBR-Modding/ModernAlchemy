package com.dyonovan.itemreplication.blocks;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.TileBlastFurnaceCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockBlastFurnaceCore extends BlockContainer {
    @SideOnly(Side.CLIENT)
    private IIcon front;

    public BlockBlastFurnaceCore() {
        super(Material.rock);
        this.setBlockName(Constants.MODID + ":blockBlastFurnaceCore");
        this.setCreativeTab(ItemReplication.tabItemReplication);
        this.setLightLevel(8F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int par2) {
        return new TileBlastFurnaceCore();
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon(Constants.MODID + ":blastFurnace");
        this.front = register.registerIcon(Constants.MODID + ":blastFurnaceFront");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int i, int j) {
        return i != 1 || i != 0 ? front : blockIcon;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            TileBlastFurnaceCore core = (TileBlastFurnaceCore)par1World.getTileEntity(par2, par3, par4);
            if(core != null) {
                par5EntityPlayer.openGui(ItemReplication.instance, 0, par1World, par2, par3, par4);
            }
            return true;
        }
    }
}
