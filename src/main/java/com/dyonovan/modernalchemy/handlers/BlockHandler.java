package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.common.blocks.arcfurnace.BlockArcFurnaceCore;
import com.dyonovan.modernalchemy.common.blocks.arcfurnace.dummies.*;
import com.dyonovan.modernalchemy.common.blocks.fluids.BlockFluidActinium;
import com.dyonovan.modernalchemy.common.blocks.fluids.BlockFluidCompressedAir;
import com.dyonovan.modernalchemy.common.blocks.machines.BlockAdvancedCrafter;
import com.dyonovan.modernalchemy.common.blocks.machines.BlockAmalgamator;
import com.dyonovan.modernalchemy.common.blocks.machines.BlockElectricBellows;
import com.dyonovan.modernalchemy.common.blocks.machines.BlockPatternRecorder;
import com.dyonovan.modernalchemy.common.blocks.ore.BlockOreActinium;
import com.dyonovan.modernalchemy.common.blocks.ore.BlockOreCopper;
import com.dyonovan.modernalchemy.common.blocks.replicator.BlockReplicatorCPU;
import com.dyonovan.modernalchemy.common.blocks.replicator.BlockReplicatorFrame;
import com.dyonovan.modernalchemy.common.blocks.replicator.BlockReplicatorStand;
import com.dyonovan.modernalchemy.common.blocks.teslacoil.BlockSuperTeslaCoil;
import com.dyonovan.modernalchemy.common.blocks.teslacoil.BlockTeslaBase;
import com.dyonovan.modernalchemy.common.blocks.teslacoil.BlockTeslaCoil;
import com.dyonovan.modernalchemy.common.blocks.teslacoil.BlockTeslaStand;
import com.dyonovan.modernalchemy.common.fluids.FluidActinium;
import com.dyonovan.modernalchemy.common.fluids.FluidCompressedAir;
import com.dyonovan.modernalchemy.common.tileentity.arcfurnace.TileArcFurnaceCore;
import com.dyonovan.modernalchemy.common.tileentity.arcfurnace.dummies.*;
import com.dyonovan.modernalchemy.common.tileentity.machines.TileAdvancedCrafter;
import com.dyonovan.modernalchemy.common.tileentity.machines.TileAmalgamator;
import com.dyonovan.modernalchemy.common.tileentity.machines.TileElectricBellows;
import com.dyonovan.modernalchemy.common.tileentity.machines.TilePatternRecorder;
import com.dyonovan.modernalchemy.common.tileentity.replicator.TileReplicatorCPU;
import com.dyonovan.modernalchemy.common.tileentity.replicator.TileReplicatorFrame;
import com.dyonovan.modernalchemy.common.tileentity.replicator.TileReplicatorStand;
import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileSuperTeslaCoil;
import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileTeslaBase;
import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileTeslaStand;
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
    public static Block blockOreActinium, blockFluidActinium, blockFluidAir, blockReplicatorStand;
    public static Block blockReplicatorCPU;
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

    @RegisterBlock(name = "blockReplicatorFrame", tileEntity = TileReplicatorFrame.class)
    public static BlockReplicatorFrame blockReplicatorFrame = new BlockReplicatorFrame();

    @RegisterBlock(name = "blockElectricBellows", tileEntity = TileElectricBellows.class)
    public static BlockElectricBellows blockElectricBellows = new BlockElectricBellows();

    @RegisterBlock(name = "blockTeslaCoil", tileEntity = TileTeslaCoil.class)
    public static BlockTeslaCoil blockTeslaCoil = new BlockTeslaCoil();

    @RegisterBlock(name = "blockSuperTeslaCoil", tileEntity = TileSuperTeslaCoil.class)
    public static BlockSuperTeslaCoil blockSuperTeslaCoil = new BlockSuperTeslaCoil();

    @RegisterBlock(name = "blockTeslaStand", tileEntity = TileTeslaStand.class)
    public static BlockTeslaStand blockTeslaStand = new BlockTeslaStand();

    @RegisterBlock(name = "blockTeslaBase", tileEntity = TileTeslaBase.class)
    public static BlockTeslaBase blockTeslaBase = new BlockTeslaBase();

    @RegisterBlock(name = "blockPatternRecorder", tileEntity = TilePatternRecorder.class)
    public static BlockPatternRecorder blockPatternRecorder = new BlockPatternRecorder();

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

        //BlockFrameEnergy
        registerBlock(blockReplicatorCPU = new BlockReplicatorCPU(), "blockReplicatorCPU", TileReplicatorCPU.class);

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