package com.dyonovan.modernalchemy.blocks.arcfurnace;

import com.dyonovan.modernalchemy.ItemReplication;
import com.dyonovan.modernalchemy.blocks.BlockBase;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.util.WorldUtils;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.TileArcFurnaceCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockArcFurnaceCore extends BlockBase {
    @SideOnly(Side.CLIENT)
    private IIcon front;

    public BlockArcFurnaceCore() {
        super(Material.rock);
        this.setBlockName(Constants.MODID + ":blockArcFurnaceCore");
        this.setCreativeTab(ItemReplication.tabItemReplication);
        this.setLightLevel(8F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int par2) {
        return new TileArcFurnaceCore();
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
        super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            TileArcFurnaceCore core = (TileArcFurnaceCore)par1World.getTileEntity(par2, par3, par4);
            if(core != null && core.isWellFormed()) {
                par5EntityPlayer.openGui(ItemReplication.instance, GuiHandler.BLAST_FURNACE_GUI_ID, par1World, par2, par3, par4);
            }
            return true;
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
    {
        TileArcFurnaceCore core = (TileArcFurnaceCore) world.getTileEntity(x, y, z);
        WorldUtils.expelItem(world, core.xCoord, core.yCoord, core.zCoord, core.inventory.getStackInSlot(0));
        WorldUtils.expelItem(world, core.xCoord, core.yCoord, core.zCoord, core.inventory.getStackInSlot(1));

        super.breakBlock(world, x, y, z, par5, par6);
    }
}
