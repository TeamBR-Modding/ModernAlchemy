package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.blocks.arcfurnace.BlockArcFurnaceCore;
import com.dyonovan.modernalchemy.blocks.arcfurnace.dummies.*;
import com.dyonovan.modernalchemy.blocks.fluids.BlockFluidActinium;
import com.dyonovan.modernalchemy.blocks.fluids.BlockFluidCompressedAir;
import com.dyonovan.modernalchemy.blocks.machines.BlockAdvancedCrafter;
import com.dyonovan.modernalchemy.blocks.machines.BlockElectricBellows;
import com.dyonovan.modernalchemy.blocks.machines.BlockPatternRecorder;
import com.dyonovan.modernalchemy.blocks.machines.BlockAmalgamator;
import com.dyonovan.modernalchemy.blocks.ore.BlockOreActinium;
import com.dyonovan.modernalchemy.blocks.ore.BlockOreCopper;
import com.dyonovan.modernalchemy.blocks.replicator.BlockReplicatorStand;
import com.dyonovan.modernalchemy.blocks.replicator.BlockReplicatorFrame;
import com.dyonovan.modernalchemy.blocks.replicator.BlockReplicatorCPU;
import com.dyonovan.modernalchemy.blocks.teslacoil.BlockTeslaBase;
import com.dyonovan.modernalchemy.blocks.teslacoil.BlockTeslaCoil;
import com.dyonovan.modernalchemy.blocks.teslacoil.BlockTeslaStand;
import com.dyonovan.modernalchemy.fluids.FluidActinium;
import com.dyonovan.modernalchemy.fluids.FluidCompressedAir;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.TileArcFurnaceCore;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.dummies.*;
import com.dyonovan.modernalchemy.tileentity.machines.TileElectricBellows;
import com.dyonovan.modernalchemy.tileentity.machines.TileAdvancedCrafter;
import com.dyonovan.modernalchemy.tileentity.machines.TilePatternRecorder;
import com.dyonovan.modernalchemy.tileentity.machines.TileAmalgamator;
import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorFrame;
import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorCPU;
import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorStand;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaBase;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaStand;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class BlockHandler {

    public static Fluid fluidActinium, fluidCompressedAir;
    public static Block blockArcFurnaceDummy, blockArcFurnaceDummyItemIO, blockArcFurnaceDummyAirValve;
    public static Block blockArcFurnaceDummyOutputValve, blockArcFurnaceDummyEnergy, blockArcFurnaceCore, blockTeslaStand, blockCoil;
    public static Block blockOreActinium, blockFluidActinium, blockElectricBellows, blockFluidAir, blockReplicatorStand;
    public static Block blockPatternRecorder, blockAmalgamator, blockTeslaBase, blockReplicatorFrame, blockReplicatorCPU;
    public static Block blockAdvancedFurnace, blockOreCopper;

    public static List<Block> blockRegistry = new ArrayList<Block>();

    public static void init() {
        //Actinium Fluid Registration
        fluidActinium = new FluidActinium();
        FluidRegistry.registerFluid(fluidActinium);
        blockFluidActinium = new BlockFluidActinium();
        GameRegistry.registerBlock(blockFluidActinium, "fluidActinium");

        //Fluid Compressed Air
        fluidCompressedAir = new FluidCompressedAir();
        FluidRegistry.registerFluid(fluidCompressedAir);
        blockFluidAir = new BlockFluidCompressedAir();
        GameRegistry.registerBlock(blockFluidAir, "blockFluidAir");

        //Ore Actinium
        registerBlock(blockOreActinium = new BlockOreActinium(), "oreActinium", null);

        //Ore Copper
        registerBlock(blockOreCopper = new BlockOreCopper(), "oreCopper", null);
        OreDictionary.registerOre("oreCopper", new ItemStack(blockOreCopper));

        //BlockElectricBellows
        registerBlock(blockElectricBellows = new BlockElectricBellows(), "blockElectricBellows", TileElectricBellows.class);

        //blockAmalgamator
        registerBlock(blockAmalgamator = new BlockAmalgamator(), "blockAmalgamator", TileAmalgamator.class);

        // Block Pattern Recorder
        registerBlock(blockPatternRecorder = new BlockPatternRecorder(), "patternRecorder", TilePatternRecorder.class);

        //ArcFurnaceCore
        registerBlock(blockArcFurnaceCore = new BlockArcFurnaceCore(), "arcFurnaceCore", TileArcFurnaceCore.class);

        //ArcFurnaceDummy
        registerBlock(blockArcFurnaceDummy = new BlockDummy("arcFurnaceDummy"), "arcFurnaceDummy", TileDummy.class);

        //ArcFurnaceDummy Energy
        registerBlock(blockArcFurnaceDummyEnergy = new BlockDummyEnergyReciever("arcFurnaceDummyEnergy"), "arcFurnaceDummyEnergy", TileDummyEnergyReciever.class);

        //ArcFurnaceDummyItemIO
        registerBlock(blockArcFurnaceDummyItemIO = new BlockItemIODummy("arcFurnaceDummyItemIO"), "arcFurnaceDummyItemIO", TileDummyItemIO.class);

        //ArcFurnaceDummyAirValue
        registerBlock(blockArcFurnaceDummyAirValve = new BlockDummyAirValve("arcFurnaceDummyAirValve"), "arcFurnaceDummyAirValve", TileDummyAirValve.class);

        //ArcFurnaceDummyOutputValue
        registerBlock(blockArcFurnaceDummyOutputValve = new BlockDummyOutputValve("arcFurnaceDummyOutputValve"), "arcFurnaceDummyOutputValve", TileDummyOutputValve.class);

        //Tesla Base
        registerBlock(blockTeslaBase = new BlockTeslaBase(), "blockTeslaBase", TileTeslaBase.class);

        //Tesla Stand
        registerBlock(blockTeslaStand = new BlockTeslaStand(), "blockTeslaStand", TileTeslaStand.class);

        //Tesla Coil
        registerBlock(blockCoil = new BlockTeslaCoil(), "blockCoil", TileTeslaCoil.class);

        //BlockFrameEnergy
        registerBlock(blockReplicatorCPU = new BlockReplicatorCPU(), "blockReplicatorCPU", TileReplicatorCPU.class);

        //BlockFrame
        registerBlock(blockReplicatorFrame = new BlockReplicatorFrame(), "blockReplicatorFrame", TileReplicatorFrame.class);

        //BlockCenterStand
        registerBlock(blockReplicatorStand = new BlockReplicatorStand(), "blockReplicatorStand", TileReplicatorStand.class);

        //BlockFurnace
        registerBlock(blockAdvancedFurnace = new BlockAdvancedCrafter(), "blockAdvancedFurnace", TileAdvancedCrafter.class);
    }

    public static void registerBlock(Block registerBlock, String name, Class<? extends TileEntity> tileEntity) {
        GameRegistry.registerBlock(registerBlock, name);
        if(tileEntity != null)
            GameRegistry.registerTileEntity(tileEntity, name);
        blockRegistry.add(registerBlock);
    }
}