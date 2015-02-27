package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.crafting.ArcFurnaceRecipeRegistry;
import com.dyonovan.modernalchemy.crafting.MAFurnaceRecipeRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.Arrays;

public class CraftingHandler {

    public static void init() {
        //Temp for Circuit TODO TEMP
        GameRegistry.addRecipe(new ItemStack(ItemHandler.itemCircuit), "CBC", "BAB", "CBC",
                'A', Items.quartz, 'B', Items.glowstone_dust, 'C', Items.iron_ingot);

        //Machine Frame
        GameRegistry.addRecipe(new ItemStack(ItemHandler.itemMachineFrame), "CBC", "B B", "CBC",
                'B', Blocks.iron_bars, 'C', Items.iron_ingot);

        //Arc Furnace Dummy
        GameRegistry.addRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummy), "BCB", "CAC", "BCB",
                'A', ItemHandler.itemMachineFrame, 'B', Items.iron_ingot, 'C', Items.gold_ingot);

        //Arc Furnace DummyOutputValue
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummyOutputValve), "BDB", "CAC", "BEB",
                'A', BlockHandler.blockArcFurnaceDummy, 'B', Items.iron_ingot, 'C', Items.bucket,
                'D', "circuitAdvanced", 'E', ItemHandler.itemActinium));

        //Arc Furnace DummyItemIO
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummyItemIO), "BDB", "CAC", "BEB",
                'A', BlockHandler.blockArcFurnaceDummy, 'B', Items.iron_ingot, 'C', Blocks.hopper,
                'D', "circuitAdvanced", 'E', ItemHandler.itemActinium));

        //Arc Furnace DummyEnergyReceiver
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummyEnergy), "BDB", "CAC", "BEB",
                'A', BlockHandler.blockArcFurnaceDummy, 'B', Items.iron_ingot, 'C', ItemHandler.itemEnergyAntenna,
                'D', "circuitAdvanced", 'E', ItemHandler.itemActinium));

        //Arc Furnace DummyEnergyCompressedAirValve
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummyAirValve), "BDB", "CAC", "BEB",
                'A', BlockHandler.blockArcFurnaceDummy, 'B', Items.iron_ingot, 'C', Blocks.piston,
                'D', "circuitAdvanced", 'E', ItemHandler.itemActinium));

        //Arc Furnace Core
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockArcFurnaceCore), "BDB", "CAC", "BDB",
                'A', BlockHandler.blockArcFurnaceDummy, 'B', Items.iron_ingot, 'C', Items.ender_eye,
                'D', "circuitAdvanced"));

        //ItemPumpModule
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.itemPumpModule), "ACA", "CBC", "ACA",
                'A', Items.iron_ingot, 'B', "circuitAdvanced", 'C', Items.redstone));

        //Compressor
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockElectricBellows), "BEB", "CAC", "BDB",
                'A', BlockHandler.blockArcFurnaceDummy, 'B', Items.iron_ingot, 'C', ItemHandler.itemPumpModule,
                'D', "circuitAdvanced", 'E', ItemHandler.itemEnergyAntenna));

        //ItemEnergyAntenna
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.itemEnergyAntenna), "ACA", "DBD", "ACA",
                'A', Items.iron_ingot, 'B', "circuitAdvanced", 'C', Items.redstone, 'D', Items.ender_pearl));

        //Solidifier
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockAmalgamator), "BEB", "CAC", "BDB",
                'A', BlockHandler.blockArcFurnaceDummy, 'B', Items.iron_ingot, 'C', Items.cauldron,
                'D', "circuitAdvanced", 'E', ItemHandler.itemEnergyAntenna));

        //Pattern Recorder
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockPatternRecorder), "BEB", "CAC", "BDB",
                'A', BlockHandler.blockArcFurnaceDummy, 'B', Items.iron_ingot, 'C', ItemHandler.laserNode,
                'D', "circuitAdvanced", 'E', ItemHandler.itemEnergyAntenna));

        //Faraday Wire
        GameRegistry.addRecipe(new ItemStack(ItemHandler.itemFaradayWire), "AB", "BA",
                'A', Blocks.iron_bars, 'B', ItemHandler.itemSteelIngot);

         //Arc Furnace Recipes
        ArcFurnaceRecipeRegistry.instance.addRecipe(ItemHandler.itemActinium, FluidContainerRegistry.BUCKET_VOLUME);
        ArcFurnaceRecipeRegistry.instance.addRecipe(ItemHandler.itemSlag,  FluidContainerRegistry.BUCKET_VOLUME / 4);

        //Advanced Furnace Recipes
        MAFurnaceRecipeRegistry.instance.addRecipe(new ArrayList<Item>(Arrays.asList(Items.coal, Items.iron_ingot)), ItemHandler.itemSteelIngot);
    }
}
