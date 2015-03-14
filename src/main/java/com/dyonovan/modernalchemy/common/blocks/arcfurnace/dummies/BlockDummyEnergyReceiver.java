package com.dyonovan.modernalchemy.common.blocks.arcfurnace.dummies;

import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockDummyEnergyReceiver extends BlockDummy {
    @SideOnly(Side.CLIENT)
    public static class Icons {
        public static IIcon inActive;
        public static IIcon active;
    }

    public BlockDummyEnergyReceiver() {
        super();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        setDefaultTexture(blockIcon = register.registerIcon(Constants.MODID + ":blastFurnaceEnergy"));
        Icons.active = register.registerIcon(Constants.MODID + ":blastFurnaceEnergyActive");
        Icons.inActive = blockIcon;
    }
}