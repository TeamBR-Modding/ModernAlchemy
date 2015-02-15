package com.dyonovan.modernalchemy.fluids;

import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraftforge.fluids.Fluid;

public class FluidActinium extends Fluid {

    public FluidActinium() {
        super("fluidActinium");

        this.setViscosity(9000);
        this.setUnlocalizedName(Constants.MODID + ":fluidActinium");

    }

}
