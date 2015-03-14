package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.blocks.arcfurnace.BlockArcFurnaceCore;
import com.dyonovan.modernalchemy.blocks.arcfurnace.dummies.*;
import com.dyonovan.modernalchemy.blocks.fluids.BlockFluidActinium;
import com.dyonovan.modernalchemy.blocks.fluids.BlockFluidCompressedAir;
import com.dyonovan.modernalchemy.blocks.machines.BlockAdvancedCrafter;
import com.dyonovan.modernalchemy.blocks.machines.BlockAmalgamator;
import com.dyonovan.modernalchemy.blocks.machines.BlockElectricBellows;
import com.dyonovan.modernalchemy.blocks.machines.BlockPatternRecorder;
import com.dyonovan.modernalchemy.blocks.ore.BlockOreActinium;
import com.dyonovan.modernalchemy.blocks.ore.BlockOreCopper;
import com.dyonovan.modernalchemy.blocks.replicator.BlockReplicatorCPU;
import com.dyonovan.modernalchemy.blocks.replicator.BlockReplicatorFrame;
import com.dyonovan.modernalchemy.blocks.replicator.BlockReplicatorStand;
import com.dyonovan.modernalchemy.blocks.teslacoil.BlockSuperTeslaCoil;
import com.dyonovan.modernalchemy.blocks.teslacoil.BlockTeslaBase;
import com.dyonovan.modernalchemy.blocks.teslacoil.BlockTeslaCoil;
import com.dyonovan.modernalchemy.blocks.teslacoil.BlockTeslaStand;
import com.dyonovan.modernalchemy.fluids.FluidActinium;
import com.dyonovan.modernalchemy.fluids.FluidCompressedAir;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.TileArcFurnaceCore;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.dummies.*;
import com.dyonovan.modernalchemy.tileentity.machines.TileAdvancedCrafter;
import com.dyonovan.modernalchemy.tileentity.machines.TileAmalgamator;
import com.dyonovan.modernalchemy.tileentity.machines.TileElectricBellows;
import com.dyonovan.modernalchemy.tileentity.machines.TilePatternRecorder;
import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorCPU;
import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorFrame;
import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorStand;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileSuperTeslaCoil;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaBase;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaStand;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import openmods.config.BlockInstances;
import openmods.config.game.RegisterBlock;

import java.util.ArrayList;
import java.util.List;

public class BlockHandler implements BlockInstances{

    public static Fluid fluidActinium, fluidCompressedAir;
    public static Block blockTeslaStand, blockCoil, blockSuperCoil;
    public static Block blockOreActinium, blockFluidActinium, blockElectricBellows, blockFluidAir, blockReplicatorStand;
    public static Block blockPatternRecorder, blockTeslaBase, blockReplicatorFrame, blockReplicatorCPU;
    public static Block blockOreCopper;

    @RegisterBlock(name = "blockAmalgamator", tileEntity = TileAmalgamator.class)
    public static BlockAmalgamator blockAmalgamator;

    @RegisterBlock(name = "blockAdvancedCrafter", tileEntity = TileAdvancedCrafter.class)
    public static BlockAdvancedCrafter blockAdvancedCrafter;

    @RegisterBlock(name = "blockArcFurnaceCore", tileEntity = TileArcFurnaceCore.class)
    public static BlockArcFurnaceCore blockArcFurnaceCore;

    @RegisterBlock(name =  "blockArcFurnaceDummy", tileEntity = TileDummy.class)
    public static BlockDummy blockArcFurnaceDummy = new BlockDummy();

    @RegisterBlock(name = "blockArcFurnaceDummyAirValve", tileEntity = TileDummyAirValve.class)
    public static BlockDummyAirValve blockArcFurnaceDummyAirValve = new BlockDummyAirValve();

    @RegisterBlock(name = "blockArcFurnaceDummyOutputValve", tileEntity = TileDummyOutputValve.class)
    public static BlockDummyOutputValve blockArcFurnaceDummyOutputValve = new BlockDummyOutputValve();

    @RegisterBlock(name = "blockArcFurnaceDummyItemIO", tileEntity = TileDummyItemIO.class)
    public static BlockItemIODummy blockArcFurnaceDummyItemIO = new BlockItemIODummy();

    @RegisterBlock(name = "blockArcFurnaceDummyEnergy", tileEntity = TileDummyEnergyReciever.class)
    public static BlockDummyEnergyReceiver blockArcFurnaceDummyEnergy = new BlockDummyEnergyReceiver();

    public static List<Block> blockRegistry = new ArrayList<Block>();

    public static void preInit() {
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
        registerBlock(blockOreActinium = new BlockOreActinium(), "blockOreActinium", null);

        //BlockElectricBellows
        registerBlock(blockElectricBellows = new BlockElectricBellows(), "blockElectricBellows", TileElectricBellows.class);


        // Block Pattern Recorder
        registerBlock(blockPatternRecorder = new BlockPatternRecorder(), "blockPatternRecorder", TilePatternRecorder.class);

        //Tesla Base
        registerBlock(blockTeslaBase = new BlockTeslaBase(), "blockTeslaBase", TileTeslaBase.class);

        //Tesla Stand
        registerBlock(blockTeslaStand = new BlockTeslaStand(), "blockTeslaStand", TileTeslaStand.class);

        //Tesla Coil
        registerBlock(blockCoil = new BlockTeslaCoil(), "blockTeslaCoil", TileTeslaCoil.class);

        //Super Tesla Coil
        registerBlock(blockSuperCoil = new BlockSuperTeslaCoil(), "blockSuperTeslaCoil", TileSuperTeslaCoil.class);

        //BlockFrameEnergy
        registerBlock(blockReplicatorCPU = new BlockReplicatorCPU(), "blockReplicatorCPU", TileReplicatorCPU.class);

        //BlockFrame
        registerBlock(blockReplicatorFrame = new BlockReplicatorFrame(), "blockReplicatorFrame", TileReplicatorFrame.class);

        //BlockCenterStand
        registerBlock(blockReplicatorStand = new BlockReplicatorStand(), "blockReplicatorStand", TileReplicatorStand.class);

    }

    public static void initCopper() {
        //Ore Copper
        registerBlock(blockOreCopper = new BlockOreCopper(), "blockOreCopper", null);
        OreDictionary.registerOre("oreCopper", new ItemStack(blockOreCopper));
    }

    public static void registerBlock(Block registerBlock, String name, Class<? extends TileEntity> tileEntity) {
        GameRegistry.registerBlock(registerBlock, name);
        if(tileEntity != null)
            GameRegistry.registerTileEntity(tileEntity, name);
        blockRegistry.add(registerBlock);
    }
}