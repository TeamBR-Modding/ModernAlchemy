package com.dyonovan.itemreplication.handlers;

import com.dyonovan.itemreplication.blocks.BlockBlastFurnaceCore;
import com.dyonovan.itemreplication.blocks.BlockDummy;
import com.dyonovan.itemreplication.blocks.BlockFluidActinium;
import com.dyonovan.itemreplication.blocks.BlockOreActinium;
import com.dyonovan.itemreplication.tiles.BaseTile;
import com.dyonovan.itemreplication.tiles.TileBlastFurnaceCore;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockHandler {

    public static Fluid fluidActinium;
    public static Block blockfluidActinium;
    public static Block blockOreActinium;
    public static Block blockBlastFurnaceDummy;
    public static Block blockBlastFurnaceCore;

    public static void init() {

        fluidActinium = new Fluid("fluidActinium").setViscosity(9000);
        FluidRegistry.registerFluid(fluidActinium);
        blockfluidActinium = new BlockFluidActinium();
        GameRegistry.registerBlock(blockfluidActinium, "fluidActinium");
        fluidActinium.setUnlocalizedName(blockfluidActinium.getUnlocalizedName());

        blockOreActinium = new BlockOreActinium();
        GameRegistry.registerBlock(blockOreActinium, "oreActinium");

        blockBlastFurnaceDummy = new BlockDummy("blastFurnaceDummy");
        GameRegistry.registerBlock(blockBlastFurnaceDummy, "blastFurnaceDummy");
        GameRegistry.registerTileEntity(BaseTile.class, "baseTile");

        blockBlastFurnaceCore = new BlockBlastFurnaceCore();
        GameRegistry.registerBlock(blockBlastFurnaceCore, "blastFurnaceCore");
        GameRegistry.registerTileEntity(TileBlastFurnaceCore.class, "blastFurnaceCore");
    }
}
