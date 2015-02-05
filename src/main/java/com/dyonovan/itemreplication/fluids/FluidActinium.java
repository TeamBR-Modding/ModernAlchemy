package com.dyonovan.itemreplication.fluids;

import com.dyonovan.itemreplication.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;

public class FluidActinium extends Fluid {

    @SideOnly(Side.CLIENT)
    protected IIcon stillIcon;
    @SideOnly(Side.CLIENT)
    protected IIcon flowingIcon;

    public FluidActinium() {
        super("fluidActinium");

        this.setViscosity(9000);
        this.setUnlocalizedName(Constants.MODID + ":fluidActinium");
        this.setIcons(stillIcon, flowingIcon);

    }

}
