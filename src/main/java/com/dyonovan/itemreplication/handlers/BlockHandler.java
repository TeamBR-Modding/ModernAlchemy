package com.dyonovan.itemreplication.handlers;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.blocks.BlockCompressor;
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
        blockCompressor = new BlockCompressor(false).setCreativeTab(ItemReplication.tabItemReplication);
        blockCompressorOn = new BlockCompressor(true);
        GameRegistry.registerBlock(blockCompressor, "blockCompressor");
        GameRegistry.registerBlock(blockCompressorOn, "blockCompressorOn");

        //BlastFurnaceDummy
        blockBlastFurnaceDummy = new BlockDummy("blastFurnaceDummy");
        GameRegistry.registerBlock(blockBlastFurnaceDummy, "blastFurnaceDummy");
        GameRegistry.registerTileEntity(BaseTile.class, "baseTile");

        //BlastFurnaceDummy
        blockBlastFurnaceCore = new BlockBlastFurnaceCore();
        GameRegistry.registerBlock(blockBlastFurnaceCore, "blastFurnaceCore");
        GameRegistry.registerTileEntity(TileBlastFurnaceCore.class, "blastFurnaceCore");
    }
}
