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
import openmods.infobook.BookDocumentation;

@BookDocumentation
public class BlockAmalgamator extends BlockModernAlchemy {

    @SideOnly(Side.CLIENT)
    public static class Icons {
        public static IIcon front;
        public static IIcon side;
    }

    public BlockAmalgamator() {
        super(Material.iron);
        this.setHardness(1.5F);
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
        setRotationMode(BlockRotationMode.FOUR_DIRECTIONS);
        setPlacementMode(BlockPlacementMode.ENTITY_ANGLE);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        this.blockIcon = iconregister.registerIcon(Constants.MODID + ":amalgamator_side");
        Icons.front = iconregister.registerIcon(Constants.MODID + ":amalgamator_front");
        Icons.side = blockIcon;

        setTexture(ForgeDirection.NORTH, blockIcon);
        setTexture(ForgeDirection.SOUTH, Icons.front);
        setTexture(ForgeDirection.EAST, blockIcon);
        setTexture(ForgeDirection.WEST, blockIcon);
        setTexture(ForgeDirection.UP, blockIcon);
        setTexture(ForgeDirection.DOWN, blockIcon);
    }
}
