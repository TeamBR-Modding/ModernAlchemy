package com.dyonovan.modernalchemy.handlers;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

public class RecipeHandler {

    public static void init() {
        //Slag to Ore
        GameRegistry.addRecipe(new ItemStack(BlockHandler.blockOreActinium), new Object[]{
                "AA","AA",'A', ItemHandler.itemSlag
        });
    }
}
