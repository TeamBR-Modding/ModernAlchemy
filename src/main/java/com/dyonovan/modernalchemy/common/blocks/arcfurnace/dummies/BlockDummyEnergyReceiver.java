package com.dyonovan.modernalchemy.common.blocks.arcfurnace.dummies;

import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import openmods.infobook.BookDocumentation;

@BookDocumentation
public class BlockDummyEnergyReceiver extends BlockDummy {
    public BlockDummyEnergyReceiver() {
        super();
        setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 1.0F, 0.7F);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return !(world.getBlock(x, y + 1, x) instanceof BlockDummy);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        setDefaultTexture(blockIcon = register.registerIcon(Constants.MODID + ":blastFurnaceEnergy"));
    }
}