package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.crafting.ArcFurnaceRecipeRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class CraftingHandler {

    public static void init() {
        //Temp for Circuit TODO TEMP
        GameRegistry.addRecipe(new ItemStack(ItemHandler.itemCircuit), "CBC", "BAB", "CBC",
                'A', Items.quartz, 'B', Items.gold_ingot, 'C', Items.iron_ingot);

        //Machine Frame
        GameRegistry.addRecipe(new ItemStack(ItemHandler.itemMachineFrame), "CBC", "B B", "CBC",
                'B', Blocks.iron_bars, 'C', Items.iron_ingot);

        //Arc Furnace Dummy
        GameRegistry.addRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummy), "BCB", "CAC", "BCB",
                'A', ItemHandler.itemMachineFrame, 'B', Items.iron_ingot, 'C', Items.gold_ingot);

        //Arc Furnace DummyOutputValue
        GameRegistry.addRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummyOutputValue), "BDB", "CAC", "BEB",
                'A', BlockHandler.blockArcFurnaceDummy, 'B', Items.iron_ingot, 'C', Items.bucket,
                'D', "circuitBasic", 'E', ItemHandler.itemActinium);

        //Arc Furnace DummyItemIO
        GameRegistry.addRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummyItemIO), "BDB", "CAC", "BEB",
                'A', BlockHandler.blockArcFurnaceDummy, 'B', Items.iron_ingot, 'C', Blocks.sticky_piston,
                'D', "circuitBasic", 'E', ItemHandler.itemActinium);

         //Arc Furnace Recipes
        ArcFurnaceRecipeRegistry.instance.addRecipe(ItemHandler.itemActinium, FluidContainerRegistry.BUCKET_VOLUME);
        ArcFurnaceRecipeRegistry.instance.addRecipe(ItemHandler.itemSlag,  FluidContainerRegistry.BUCKET_VOLUME / 4);
    }
}
