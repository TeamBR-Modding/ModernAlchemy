package com.dyonovan.modernalchemy.nei;


import codechicken.nei.recipe.GuiCraftingRecipe;
import com.dyonovan.modernalchemy.container.ContainerAdvancedCrafter;
import com.dyonovan.modernalchemy.container.ContainerAmalgamator;
import com.dyonovan.modernalchemy.container.ContainerArcFurnace;
import net.minecraft.inventory.Container;

public class NEICallback implements INEICallback {

    @Override
    public void onArrowClicked(Container gui) {
        if(gui instanceof ContainerArcFurnace)
            GuiCraftingRecipe.openRecipeGui("modernalchemy.arcfurnace.recipes");
        if(gui instanceof ContainerAdvancedCrafter)
            GuiCraftingRecipe.openRecipeGui("modernalchemy.advancedCrafter.recipes");
        if(gui instanceof ContainerAmalgamator)
            GuiCraftingRecipe.openRecipeGui("modernalchemy.solidifier.recipes");
    }
}

