package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.crafting.AdvancedCrafterRecipeRegistry;
import com.dyonovan.modernalchemy.crafting.ArcFurnaceRecipeRegistry;
import com.dyonovan.modernalchemy.tileentity.machines.TileAdvancedCrafter;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class CraftingHandler {

    public static void preInit() {
        //Advanced Crafter
        GameRegistry.addRecipe(new ItemStack(BlockHandler.blockAdvancedFurnace), "ABA", "CDC", "AEA",
                'A', Items.iron_ingot, 'B', Blocks.hopper, 'C', Blocks.piston,
                'D', Blocks.furnace, 'E', Blocks.redstone_block);

        //Elec Bellows
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockElectricBellows), "ABA", "CDE", "AFA",
                'A', "plateSteel", 'B', ItemHandler.itemEnergyAntenna, 'C', Blocks.piston,
                'D', ItemHandler.itemMachineFrame, 'E', ItemHandler.itemPumpModule, 'F', "circuitAdvanced"));

        //Amagamator
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockAmalgamator), "ABA", "CDE", "AFA",
                'A', "plateSteel", 'B', ItemHandler.itemEnergyAntenna, 'C', Blocks.crafting_table,
                'D', ItemHandler.itemMachineFrame, 'E', Blocks.cauldron, 'F', "circuitAdvanced"));

        //Pattern Recorder
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockPatternRecorder), "ABA", "CDC", "AEA",
                'A', "plateSteel", 'B', ItemHandler.itemEnergyAntenna, 'C', ItemHandler.laserNode,
                'D', ItemHandler.itemMachineFrame, 'E', "circuitAdvanced"));

        //Arc Furnace Processor
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockArcFurnaceCore), "ABA", "CDC", "ABA",
                'A', "plateSteel", 'B', "circuitAdvanced", 'C', "gearSteel",
                'D', BlockHandler.blockArcFurnaceDummy));

        //Arc Furnace Dummy
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummy), "ABA", "BCB", "ABA",
                'A', "plateSteel", 'B', "ingotCopper", 'C', ItemHandler.itemMachineFrame));

        //Arc Furnace Output Valve
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummyOutputValve), "ABA", "CDC", "AEA",
                'A', "plateSteel", 'B', "circuitAdvanced", 'C', Items.bucket, 'D', BlockHandler.blockArcFurnaceDummy,
                'E', ItemHandler.itemActiniumDust));

        //Arc Furnace Item IO
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummyItemIO), "ABA", "CDC", "AEA",
                'A', "plateSteel", 'B', "circuitAdvanced", 'C', Items.bucket, 'D', BlockHandler.blockArcFurnaceDummy,
                'E', ItemHandler.itemActiniumDust));

        //Arc Furnace Energy Rec
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummyEnergy), "ABA", "CDC", "AEA",
                'A', "plateSteel", 'B', "circuitAdvanced", 'C', ItemHandler.itemCapacator, 'D', BlockHandler.blockArcFurnaceDummy,
                'E', "circuitAdvanced"));

        //Arc Furnace Compressed Air Valve
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummyItemIO), "ABA", "CDC", "AEA",
                'A', "plateSteel", 'B', "circuitAdvanced", 'C', Blocks.piston, 'D', BlockHandler.blockArcFurnaceDummy,
                'E', ItemHandler.itemActiniumDust));

        //Replicator Controller
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockReplicatorCPU), "ABA", "CDC", "CEC",
                'A', ItemHandler.itemSteelTube, 'B', BlockHandler.blockReplicatorFrame, 'C', ItemHandler.itemMachineFrame,
                'D', "circuitAdvanced", 'E', ItemHandler.itemCapacator));

        //Replicator Frame
        GameRegistry.addRecipe(new ItemStack(BlockHandler.blockReplicatorFrame, 8), "ABA", "BCB", "ABA",
                'A', ItemHandler.itemSteelTube, 'B', ItemHandler.itemMachineFrame, 'C', Blocks.iron_bars);

        //Replicator Stand
        GameRegistry.addRecipe(new ItemStack(BlockHandler.blockReplicatorStand, 8), "ABA", "ACA",
                'A', ItemHandler.itemMachineFrame, 'B', ItemHandler.itemReplicatorPattern, 'C', Blocks.anvil);

        //Replicator Controller
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.laserNode), "ABA", "CDC", "AEA",
                'A', BlockHandler.blockReplicatorFrame, 'B', ItemHandler.itemCapacator, 'C', ItemHandler.itemCopperCoil,
                'D', "circuitAdvanced", 'E', ItemHandler.itemDenseCopperCoil));

        //Tesla Coil Top
        GameRegistry.addRecipe(new ItemStack(BlockHandler.blockCoil), "ABA", "CDC", " D ",
                'A', ItemHandler.itemDenseCopperCoil, 'B', Blocks.iron_bars, 'C', ItemHandler.itemCopperCoil,
                'D', ItemHandler.itemSteelTube);

        //Tesla Coil Stand
        GameRegistry.addRecipe(new ItemStack(BlockHandler.blockTeslaStand), "ABA", "ABA", "ABA",
                'A', ItemHandler.itemCopperCoil, 'B', ItemHandler.itemSteelTube);

        //Tesla Coil Base
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockTeslaBase), "ABA", "CDC", "AEA",
                'A', "plateSteel", 'B', ItemHandler.itemSteelTube, 'C', ItemHandler.itemCopperCoil,
                'D', "circuitAdvanced", 'E', ItemHandler.itemCapacator));

        //Wrench
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.itemWrench), "A A", " A ", " A ",
                'A', "plateSteel"));

        //Blank Pattern
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.itemReplicatorPattern), "AAA", "ABA", "AAA",
                'A', ItemHandler.itemMemory, 'B', "circuitAdvanced"));

        //Graphene //TODO




        //Arc Furnace Recipes
        ArcFurnaceRecipeRegistry.instance.addRecipe(ItemHandler.itemActiniumDust, FluidContainerRegistry.BUCKET_VOLUME);
        ArcFurnaceRecipeRegistry.instance.addRecipe(ItemHandler.itemSlag,  FluidContainerRegistry.BUCKET_VOLUME / 4);
        ArcFurnaceRecipeRegistry.instance.addRecipe(Item.getItemFromBlock(BlockHandler.blockOreActinium), FluidContainerRegistry.BUCKET_VOLUME * 2);

        //Advanced Crafting Recipes
        AdvancedCrafterRecipeRegistry.instance.addRecipe(new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Items.coal), new ItemStack(Items.iron_ingot))),
                new ItemStack(ItemHandler.itemSteelIngot, 1), 1000, TileAdvancedCrafter.COOK);

        //Furnace Recipes
        GameRegistry.addSmelting(BlockHandler.blockOreCopper, new ItemStack(ItemHandler.itemCopperIngot), 0.1f);
    }

    public static void init() {
        AdvancedCrafterRecipeRegistry.instance.addOreDictRecipe(new ArrayList<Object>(Arrays.asList("oreCopper")),
                new ItemStack(ItemHandler.itemCopperIngot, 1), 200, TileAdvancedCrafter.COOK);
        AdvancedCrafterRecipeRegistry.instance.addOreDictRecipe(new ArrayList<Object>(Arrays.asList("ingotCopper")),
                new ItemStack(ItemHandler.itemCopperWire, 3), 600, TileAdvancedCrafter.EXTRUDE);
        AdvancedCrafterRecipeRegistry.instance.addOreDictRecipe(new ArrayList<Object>(Arrays.asList("ingotSteel")),
                new ItemStack(ItemHandler.itemSteelPlate, 1), 800, TileAdvancedCrafter.BEND);

        Map recipes = FurnaceRecipes.smelting().getSmeltingList();
        Iterator i = recipes.entrySet().iterator();
        while(i.hasNext()) {
            Map.Entry<ItemStack, ItemStack> recipe = (Map.Entry<ItemStack, ItemStack>)i.next();
            AdvancedCrafterRecipeRegistry.instance.addRecipe(new ArrayList<ItemStack>(Arrays.asList(recipe.getKey())),
                    recipe.getValue().getItemDamage() >= 32767 ? new ItemStack(recipe.getValue().getItem(), recipe.getValue().stackSize, 0) : recipe.getValue(), 100, TileAdvancedCrafter.FURNACE);
            i.remove();
        }
    }
}
