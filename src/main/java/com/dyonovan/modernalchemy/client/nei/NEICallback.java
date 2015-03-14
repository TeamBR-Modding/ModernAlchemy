package com.dyonovan.modernalchemy.client.nei;


import codechicken.nei.recipe.GuiCraftingRecipe;
import com.dyonovan.modernalchemy.client.gui.INeiProvider;
import com.dyonovan.modernalchemy.common.container.BaseContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.inventory.Container;

public class NEICallback implements INEICallback {

    @Override
    public void onArrowClicked(Gui gui) {
        if(gui instanceof INeiProvider)
            GuiCraftingRecipe.openRecipeGui(((INeiProvider)gui).getNeiLabel());
    }
}

