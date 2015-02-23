package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.crafting.RecipeArcFurnace;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

public class CraftingHandler {

    public static void init() {
        //Slag to Ore
        GameRegistry.addRecipe(new ItemStack(BlockHandler.blockOreActinium), "AA", "AA", 'A', ItemHandler.itemSlag);

         //Arc Furnace Recipes
        RecipeArcFurnace.instance.addRecipe(ItemHandler.itemActinium, FluidContainerRegistry.BUCKET_VOLUME);
    }
}
