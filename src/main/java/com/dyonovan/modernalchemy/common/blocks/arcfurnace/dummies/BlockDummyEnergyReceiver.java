package com.dyonovan.modernalchemy.common.blocks.arcfurnace.dummies;

import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockDummyEnergyReceiver extends BlockDummy {
    public BlockDummyEnergyReceiver() {
        super();
        setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 1.0F, 0.7F);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        setDefaultTexture(blockIcon = register.registerIcon(Constants.MODID + ":blastFurnaceEnergy"));
    }
}