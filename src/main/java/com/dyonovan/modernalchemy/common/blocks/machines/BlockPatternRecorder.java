package com.dyonovan.modernalchemy.common.blocks.machines;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.common.blocks.BlockModernAlchemy;
import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.block.BlockRotationMode;

public class BlockPatternRecorder extends BlockModernAlchemy {

    @SideOnly(Side.CLIENT)
    public static class Icons {
        public static IIcon front;
        public static IIcon side;
    }

    public BlockPatternRecorder()
    {
        super(Material.iron);
        setRotationMode(BlockRotationMode.FOUR_DIRECTIONS);
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        this.blockIcon = iconregister.registerIcon(Constants.MODID + ":patternrecorder_side");
        Icons.front = iconregister.registerIcon(Constants.MODID + ":patternrecorder_front");
        Icons.side = blockIcon;

        setTexture(ForgeDirection.NORTH, blockIcon);
        setTexture(ForgeDirection.SOUTH, Icons.front);
        setTexture(ForgeDirection.EAST, blockIcon);
        setTexture(ForgeDirection.WEST, blockIcon);
        setTexture(ForgeDirection.UP, blockIcon);
        setTexture(ForgeDirection.DOWN, blockIcon);
    }
}
