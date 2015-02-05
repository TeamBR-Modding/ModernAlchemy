package com.dyonovan.itemreplication.handlers;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.blocks.*;
import com.dyonovan.itemreplication.blocks.*;
import com.dyonovan.itemreplication.tileentity.BaseTile;
import com.dyonovan.itemreplication.tileentity.TECompressor;
import com.dyonovan.itemreplication.tileentity.TileBlastFurnaceCore;
import com.dyonovan.itemreplication.tileentity.TileDummy;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockHandler {

    public static Fluid fluidActinium;
    public static Block blockBlastFurnaceDummy, blockBlastFurnaceCore;
    public static Block blockOreActinium, blockfluidActinium, blockCompressor, blockCompressorOn;

    public static void init() {
        //Actinium Fluid Registration
        fluidActinium = new Fluid("fluidActinium").setViscosity(9000);
        FluidRegistry.registerFluid(fluidActinium);
        blockfluidActinium = new BlockFluidActinium();
        GameRegistry.registerBlock(blockfluidActinium, "fluidActinium");
        fluidActinium.setUnlocalizedName(blockfluidActinium.getUnlocalizedName());

        //Ore Actinium
        blockOreActinium = new BlockOreActinium();
        GameRegistry.registerBlock(blockOreActinium, "oreActinium");

        //Block Compressor
        blockCompressor = new BlockCompressor();
        GameRegistry.registerBlock(blockCompressor, "blockCompressor");
        GameRegistry.registerTileEntity(TECompressor.class, "teCompressor");

        //BlastFurnaceDummy
        blockBlastFurnaceDummy = new BlockDummy("blastFurnaceDummy");
        GameRegistry.registerBlock(blockBlastFurnaceDummy, "blastFurnaceDummy");
        GameRegistry.registerTileEntity(TileDummy.class, "tileDummy");

        //BlastFurnaceCore
        blockBlastFurnaceCore = new BlockBlastFurnaceCore();
        GameRegistry.registerBlock(blockBlastFurnaceCore, "blastFurnaceCore");
        GameRegistry.registerTileEntity(TileBlastFurnaceCore.class, "blastFurnaceCore");
    }
}
