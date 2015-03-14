package com.dyonovan.modernalchemy.common.blocks.arcfurnace;

import com.dyonovan.modernalchemy.common.blocks.BlockModernAlchemy;
import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockArcFurnaceCore extends BlockModernAlchemy {
    @SideOnly(Side.CLIENT)
    private IIcon front;

    public BlockArcFurnaceCore() {
        super(Material.rock);
        this.setLightLevel(8F);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        setDefaultTexture(blockIcon = iconregister.registerIcon(Constants.MODID + ":blastFurnaceFront"));
    }
}
