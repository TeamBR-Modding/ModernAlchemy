package com.dyonovan.itemreplication.fluids;

import com.dyonovan.itemreplication.lib.Constants;
import net.minecraftforge.fluids.Fluid;

public class FluidCompressedAir extends Fluid {
    public FluidCompressedAir() {
        super("fluidCompressedAir");

        this.setViscosity(250);
        this.setUnlocalizedName(Constants.MODID + ":fluidCompressedAir");
        this.setGaseous(true);
    }
}
