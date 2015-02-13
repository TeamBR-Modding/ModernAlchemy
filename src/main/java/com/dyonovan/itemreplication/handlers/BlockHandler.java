package com.dyonovan.itemreplication.handlers;

import com.dyonovan.itemreplication.blocks.arcfurnace.BlockArcFurnaceCore;
import com.dyonovan.itemreplication.blocks.arcfurnace.dummies.BlockDummy;
import com.dyonovan.itemreplication.blocks.arcfurnace.dummies.BlockDummyAirValve;
import com.dyonovan.itemreplication.blocks.arcfurnace.dummies.BlockDummyOutputValve;
import com.dyonovan.itemreplication.blocks.arcfurnace.dummies.BlockItemIODummy;
import com.dyonovan.itemreplication.blocks.fluids.BlockFluidActinium;
import com.dyonovan.itemreplication.blocks.fluids.BlockFluidCompressedAir;
import com.dyonovan.itemreplication.blocks.machines.BlockCompressor;
import com.dyonovan.itemreplication.blocks.machines.BlockPatternRecorder;
import com.dyonovan.itemreplication.blocks.machines.BlockSolidifier;
import com.dyonovan.itemreplication.blocks.ore.BlockOreActinium;
import com.dyonovan.itemreplication.blocks.replicator.BlockReplicatorStand;
import com.dyonovan.itemreplication.blocks.replicator.BlockFrame;
import com.dyonovan.itemreplication.blocks.replicator.BlockReplicatorCPU;
import com.dyonovan.itemreplication.blocks.teslacoil.BlockTeslaBase;
import com.dyonovan.itemreplication.blocks.teslacoil.BlockTeslaCoil;
import com.dyonovan.itemreplication.blocks.teslacoil.BlockTeslaStand;
import com.dyonovan.itemreplication.fluids.FluidActinium;
import com.dyonovan.itemreplication.fluids.FluidCompressedAir;
import com.dyonovan.itemreplication.tileentity.arcfurnace.TileArcFurnaceCore;
import com.dyonovan.itemreplication.tileentity.arcfurnace.dummies.TileDummy;
import com.dyonovan.itemreplication.tileentity.arcfurnace.dummies.TileDummyAirValve;
import com.dyonovan.itemreplication.tileentity.arcfurnace.dummies.TileDummyItemIO;
import com.dyonovan.itemreplication.tileentity.arcfurnace.dummies.TileDummyOutputValve;
import com.dyonovan.itemreplication.tileentity.machines.TileCompressor;
import com.dyonovan.itemreplication.tileentity.machines.TilePatternRecorder;
import com.dyonovan.itemreplication.tileentity.machines.TileSolidifier;
import com.dyonovan.itemreplication.tileentity.replicator.TileFrame;
import com.dyonovan.itemreplication.tileentity.replicator.ReplicatorCPU;
import com.dyonovan.itemreplication.tileentity.replicator.TileReplicatorStand;
import com.dyonovan.itemreplication.tileentity.teslacoil.TileTeslaBase;
import com.dyonovan.itemreplication.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.itemreplication.tileentity.teslacoil.TileTeslaStand;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockHandler {

    public static Fluid fluidActinium, fluidCompressedAir;
    public static Block blockArcFurnaceDummy, blockArcFurnaceDummyItemIO, blockArcFurnaceDummyAirValue, blockArcFurnaceDummyOutputValue, blockArcFurnaceCore, blockTeslaStand, blockCoil;
    public static Block blockOreActinium, blockFluidActinium, blockCompressor, blockFluidAir, blockReplicatorStand;
    public static Block blockPatternRecorder, blockSolidifier, blockTeslaBase, blockFrame, blockFrameEnergy;

    public static void init() {
        //Actinium Fluid Registration
        fluidActinium = new FluidActinium();
        FluidRegistry.registerFluid(fluidActinium);
        blockFluidActinium = new BlockFluidActinium();
        GameRegistry.registerBlock(blockFluidActinium, "fluidActinium");

        //Ore Actinium
        blockOreActinium = new BlockOreActinium();
        GameRegistry.registerBlock(blockOreActinium, "oreActinium");

        //Block Compressor
        blockCompressor = new BlockCompressor();
        GameRegistry.registerBlock(blockCompressor, "blockCompressor");
        GameRegistry.registerTileEntity(TileCompressor.class, "teCompressor");

        //ArcFurnaceDummy
        blockArcFurnaceDummy = new BlockDummy("arcFurnaceDummy");
        GameRegistry.registerBlock(blockArcFurnaceDummy, "arcFurnaceDummy");
        GameRegistry.registerTileEntity(TileDummy.class, "tileDummy");

        //ArcFurnaceDummyItemIO
        blockArcFurnaceDummyItemIO = new BlockItemIODummy("arcFurnaceDummyItemIO");
        GameRegistry.registerBlock(blockArcFurnaceDummyItemIO, "arcFurnaceDummyItemIO");
        GameRegistry.registerTileEntity(TileDummyItemIO.class, "tileDummyItemIO");

        //ArcFurnaceDummyAirValue
        blockArcFurnaceDummyAirValue = new BlockDummyAirValve("arcFurnaceDummyAirValue");
        GameRegistry.registerBlock(blockArcFurnaceDummyAirValue, "arcFurnaceDummyAirValve");
        GameRegistry.registerTileEntity(TileDummyAirValve.class, "tileDummyAirValve");

        //ArcFurnaceDummyOutputValue
        blockArcFurnaceDummyOutputValue = new BlockDummyOutputValve("arcFurnaceDummyOutputValue");
        GameRegistry.registerBlock(blockArcFurnaceDummyOutputValue, "arcFurnaceDummyOutputValve");
        GameRegistry.registerTileEntity(TileDummyOutputValve.class, "tileDummyOutputValve");

        // Block Pattern Recorder
        blockPatternRecorder = new BlockPatternRecorder();
        GameRegistry.registerBlock(blockPatternRecorder, "patternRecorder");
        GameRegistry.registerTileEntity(TilePatternRecorder.class, "patternRecorder");

        //ArcFurnaceCore
        blockArcFurnaceCore = new BlockArcFurnaceCore();
        GameRegistry.registerBlock(blockArcFurnaceCore, "arcFurnaceCore");
        GameRegistry.registerTileEntity(TileArcFurnaceCore.class, "arcFurnaceCore");

        //Fluid Compressed Air
        fluidCompressedAir = new FluidCompressedAir();
        FluidRegistry.registerFluid(fluidCompressedAir);
        blockFluidAir = new BlockFluidCompressedAir();
        GameRegistry.registerBlock(blockFluidAir, "blockFluidAir");

        //Tesla Base
        blockTeslaBase = new BlockTeslaBase();
        GameRegistry.registerBlock(blockTeslaBase, "blockTeslaBase");
        GameRegistry.registerTileEntity(TileTeslaBase.class, "blockTeslaBase");

        //Tesla Stand
        blockTeslaStand = new BlockTeslaStand();
        GameRegistry.registerBlock(blockTeslaStand, "blockTeslaStand");
        GameRegistry.registerTileEntity(TileTeslaStand.class, "blockTeslaStand");

        //Tesla Coil
        blockCoil = new BlockTeslaCoil();
        GameRegistry.registerBlock(blockCoil, "blockCoil");
        GameRegistry.registerTileEntity(TileTeslaCoil.class, "blockCoil");

        //Solidifier
        blockSolidifier = new BlockSolidifier();
        GameRegistry.registerBlock(blockSolidifier, "blockSolidifier");
        GameRegistry.registerTileEntity(TileSolidifier.class, "blockSolidifier");

        //BlockFrame
        blockFrame = new BlockFrame();
        GameRegistry.registerBlock(blockFrame, "blockFrame");
        GameRegistry.registerTileEntity(TileFrame.class, "blockFrame");

        //BlockFrameEnergy
        blockFrameEnergy = new BlockReplicatorCPU();
        GameRegistry.registerBlock(blockFrameEnergy, "blockFrameEnergy");
        GameRegistry.registerTileEntity(ReplicatorCPU.class, "blockFrameEnergy");

        //BlockCenterStand
        blockReplicatorStand = new BlockReplicatorStand();
        GameRegistry.registerBlock(blockReplicatorStand, "blockReplicatorStand");
        GameRegistry.registerTileEntity(TileReplicatorStand.class, "blockReplicatorStand");
    }
}