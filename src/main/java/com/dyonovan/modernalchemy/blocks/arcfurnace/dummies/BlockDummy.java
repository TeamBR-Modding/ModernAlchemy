package com.dyonovan.modernalchemy.blocks.arcfurnace.dummies;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.blocks.BlockBase;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.manual.component.ComponentBase;
import com.dyonovan.modernalchemy.manual.component.ComponentItemRender;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.BaseCore;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.dummies.TileDummy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockDummy extends BlockBase {

    @SideOnly(Side.CLIENT)
    protected IIcon active;

    public BlockDummy(String name) {
        super(Material.rock);
        this.setBlockName(Constants.MODID + ":" + name);
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int par2) {
        return new TileDummy();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);

        TileDummy dummy = (TileDummy)world.getTileEntity(x, y, z);
        if(dummy != null && !player.isSneaking()) {
            if(dummy.getCore() != null) {
                BaseCore core = dummy.getCore();
                return core.getBlockType().onBlockActivated(world, core.xCoord, core.yCoord, core.zCoord, player, par6, par7, par8, par9);
            }
        }
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        TileDummy dummy = (TileDummy)world.getTileEntity(x, y, z);
        BaseCore core = dummy.getCore();
        if(core != null)
            core.setDirty();

        super.breakBlock(world, x, y, z, par5, par6);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon(Constants.MODID + ":blastFurnace");
        active = register.registerIcon(Constants.MODID + ":blastFurnaceActive");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta == 1 ? active : blockIcon;
    }
}
