package com.dyonovan.modernalchemy.client.nei;


import codechicken.nei.recipe.GuiCraftingRecipe;
import com.dyonovan.modernalchemy.common.container.BaseContainer;
import net.minecraft.inventory.Container;

public class NEICallback implements INEICallback {

    @Override
    public void onArrowClicked(Container gui) {
        if(gui instanceof BaseContainer)
            GuiCraftingRecipe.openRecipeGui((((BaseContainer) gui).getNEILabel()));
    }
}

