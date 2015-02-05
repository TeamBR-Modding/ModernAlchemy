package com.dyonovan.itemreplication.fluids;

import com.dyonovan.itemreplication.lib.Constants;
import net.minecraftforge.fluids.Fluid;

public class FluidActinium extends Fluid {

    public FluidActinium() {
        super("fluidActinium");

        this.setViscosity(9000);
        this.setUnlocalizedName(Constants.MODID + ":fluidActinium");

    }

}
