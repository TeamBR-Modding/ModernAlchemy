package com.dyonovan.modernalchemy.blocks.arcfurnace.dummies;

import com.dyonovan.modernalchemy.blocks.BlockModernAlchemy;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.BaseCore;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.dummies.TileDummy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockDummy extends BlockModernAlchemy {

    @SideOnly(Side.CLIENT)
    public static class Icons {
        public static IIcon inActive;
        public static IIcon active;
    }

    public BlockDummy() {
        super(Material.rock);
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
        setDefaultTexture(blockIcon = register.registerIcon(Constants.MODID + ":blastFurnace"));
        Icons.active = register.registerIcon(Constants.MODID + ":blastFurnaceActive");
        Icons.inActive = blockIcon;
    }
}
