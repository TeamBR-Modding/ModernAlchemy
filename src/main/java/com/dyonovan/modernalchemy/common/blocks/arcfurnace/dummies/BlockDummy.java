package com.dyonovan.modernalchemy.common.blocks.arcfurnace.dummies;

import com.dyonovan.modernalchemy.client.renderer.arcfurnace.ArcFurnaceDummyRenderer;
import com.dyonovan.modernalchemy.common.blocks.BlockModernAlchemy;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.common.tileentity.arcfurnace.BaseCore;
import com.dyonovan.modernalchemy.common.tileentity.arcfurnace.dummies.TileDummy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import openmods.infobook.BookDocumentation;

@BookDocumentation
public class BlockDummy extends BlockModernAlchemy {

    public BlockDummy() {
        super(Material.rock);
        setLightOpacity(1);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        TileDummy dummy = (TileDummy)world.getTileEntity(x, y, z);
        BaseCore core = dummy.getCore();
        if(core != null)
            core.setDirty();

        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return ArcFurnaceDummyRenderer.dummyRenderID;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        setDefaultTexture(blockIcon = register.registerIcon(Constants.MODID + ":blastFurnace"));
    }
}
