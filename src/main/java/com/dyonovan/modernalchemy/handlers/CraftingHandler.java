package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.crafting.ArcFurnaceResipeRegistry;
import com.dyonovan.modernalchemy.crafting.RecipeArcFurnace;
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
                'A', Items.diamond, 'B', Items.gold_ingot, 'C', Items.iron_ingot);

        //Machine Frame
        GameRegistry.addRecipe(new ItemStack(ItemHandler.itemMachineFrame), "CBC", "B B", "CBC",
                'B', Blocks.iron_bars, 'C', Items.iron_ingot);

         //Arc Furnace Recipes
        ArcFurnaceResipeRegistry.instance.addRecipe(ItemHandler.itemActinium, FluidContainerRegistry.BUCKET_VOLUME);
        ArcFurnaceResipeRegistry.instance.addRecipe(ItemHandler.itemSlag,  FluidContainerRegistry.BUCKET_VOLUME / 4);
    }
}
