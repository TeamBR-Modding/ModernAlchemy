package com.dyonovan.itemreplication.handlers;

import com.dyonovan.itemreplication.blocks.BlockFluidTechnetium;
import com.dyonovan.itemreplication.blocks.oreTechnetium;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockHandler {

    public static Fluid fluidTechnetium;
    public static Block blockfluidTechnetium;
    public static Block blockOreTechnetium;

    public static void init() {

        fluidTechnetium = new Fluid("fluidTechnetium");
        FluidRegistry.registerFluid(fluidTechnetium);
        blockfluidTechnetium = new BlockFluidTechnetium();
        GameRegistry.registerBlock(blockfluidTechnetium, "fluidTechnetium");
        fluidTechnetium.setUnlocalizedName(blockfluidTechnetium.getUnlocalizedName());

        blockOreTechnetium = new oreTechnetium();
        GameRegistry.registerBlock(blockOreTechnetium, "oreTechnetium");
        //blockOreTechnetium.setBlockTextureName("itemreplication:technetium");
    }
}
