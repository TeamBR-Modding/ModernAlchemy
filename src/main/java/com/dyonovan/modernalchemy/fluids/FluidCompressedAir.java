package com.dyonovan.modernalchemy.fluids;

import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraftforge.fluids.Fluid;

public class FluidCompressedAir extends Fluid {
    public FluidCompressedAir() {
        super("fluidCompressedAir");

        this.setViscosity(250);
        this.setUnlocalizedName(Constants.MODID + ":fluidCompressedAir");
        this.setGaseous(true);
    }
}
