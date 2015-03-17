package com.dyonovan.modernalchemy.common.blocks.machines;

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
public class BlockElectricBellows extends BlockModernAlchemy {

    @SideOnly(Side.CLIENT)
    private static class Icons {
        public static IIcon front, side;
    }

    public BlockElectricBellows() {
        super(Material.iron);
        setRotationMode(BlockRotationMode.FOUR_DIRECTIONS);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        Icons.side = iconregister.registerIcon(Constants.MODID + ":bellows_side");
        Icons.front = iconregister.registerIcon(Constants.MODID + ":bellows_front");

        setTexture(ForgeDirection.NORTH, Icons.side);
        setTexture(ForgeDirection.SOUTH, Icons.front);
        setTexture(ForgeDirection.EAST, Icons.side);
        setTexture(ForgeDirection.WEST, Icons.side);
        setTexture(ForgeDirection.UP, Icons.side);
        setTexture(ForgeDirection.DOWN, Icons.side);
    }
}


