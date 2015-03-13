package com.dyonovan.teambrcore.nei;


import codechicken.nei.recipe.GuiCraftingRecipe;
import com.dyonovan.teambrcore.container.BaseContainer;
import net.minecraft.inventory.Container;

public class NEICallback implements INEICallback {

    @Override
    public void onArrowClicked(Container gui) {
        if(gui instanceof BaseContainer)
            GuiCraftingRecipe.openRecipeGui((((BaseContainer) gui).getNEILabel()));
    }
}

