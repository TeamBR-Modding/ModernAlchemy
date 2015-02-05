package com.dyonovan.itemreplication.handlers;

import com.dyonovan.itemreplication.blocks.BlockFluidActinium;
import com.dyonovan.itemreplication.blocks.OreActinium;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockHandler {

    public static Fluid fluidActinium;
    public static Block blockfluidActinium;
    public static Block blockOreActinium;

    public static void init() {

        fluidActinium = new Fluid("fluidActinium").setViscosity(9000);
        FluidRegistry.registerFluid(fluidActinium);
        blockfluidActinium = new BlockFluidActinium();
        GameRegistry.registerBlock(blockfluidActinium, "fluidActinium");
        fluidActinium.setUnlocalizedName(blockfluidActinium.getUnlocalizedName());

        blockOreActinium = new OreActinium();
        GameRegistry.registerBlock(blockOreActinium, "oreActinium");
        //blockOreActinium.setBlockTextureName("itemreplication:technetium");
    }
}
