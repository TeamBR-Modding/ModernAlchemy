package com.dyonovan.itemreplication.blocks;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.handlers.GuiHandler;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.TilePatternRecorder;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Tim on 2/4/2015.
 */
public class BlockPatternRecorder extends BlockContainer {

    public BlockPatternRecorder()
    {
        super(Material.anvil);
        this.setBlockName(Constants.MODID + ":blockPatternRecorder");
        this.setCreativeTab(ItemReplication.tabItemReplication);
        //this.setLightLevel(8F);
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
            TilePatternRecorder tile = (TilePatternRecorder)par1World.getTileEntity(par2, par3, par4);
            if(tile != null) {
                par5EntityPlayer.openGui(ItemReplication.instance, GuiHandler.PATTERN_RECORDER_GUI_ID, par1World, par2, par3, par4);
            }
            return true;
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon(Constants.MODID + ":patternRecorder");
        //this.front = register.registerIcon(Constants.MODID + ":blastFurnaceFront");
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        super.breakBlock(world, x, y, z, block, metadata);
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int eventID, int eventParam) {
        return super.onBlockEventReceived(world, x, y, z, eventID, eventParam);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TilePatternRecorder();
    }
}
