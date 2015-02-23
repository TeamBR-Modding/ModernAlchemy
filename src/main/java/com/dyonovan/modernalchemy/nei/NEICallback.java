package com.dyonovan.modernalchemy.nei;


import codechicken.nei.recipe.GuiCraftingRecipe;
import com.dyonovan.modernalchemy.container.ContainerArcFurnace;
import net.minecraft.inventory.Container;

public class NEICallback implements INEICallback {

    @Override
    public void onArrowClicked(Container gui) {
        if(gui instanceof ContainerArcFurnace)
            GuiCraftingRecipe.openRecipeGui("modernalchemy.arcfurnace.recipes");
    }
}

