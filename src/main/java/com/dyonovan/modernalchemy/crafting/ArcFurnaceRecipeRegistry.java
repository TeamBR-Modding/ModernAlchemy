package com.dyonovan.modernalchemy.crafting;

import com.dyonovan.modernalchemy.helpers.LogHelper;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ArcFurnaceRecipeRegistry {

    /**
     * The instance of the recipies
     */
    public static ArcFurnaceRecipeRegistry instance = new ArcFurnaceRecipeRegistry();

    /**
     * The stored recipes
     */
    public List<RecipeArcFurnace> recipes = new ArrayList<RecipeArcFurnace>();

    /**
     * Adds a recipe to the registry if it doesn't already have a value
     * @param item Input item
     * @param value Output value in mb
     */
    public void addRecipe(Item item, int value) {
        if(!recipes.contains(new RecipeArcFurnace(item, value)))
            recipes.add(new RecipeArcFurnace(item, value));
        else
            LogHelper.warning("Someone tried to add a recipe for " + item.getUnlocalizedName() + " when it already exists. Aborting.");
    }

    /**
     * Get the fluid output of a given item
     * @param input The item to check {@link net.minecraft.item.Item}
     * @return Output value in mb {@link com.dyonovan.modernalchemy.common.blocks.fluids.BlockFluidActinium}
     */
    public int getReturn(Item input) {
        for(RecipeArcFurnace recipe : recipes) {
            if(recipe.getInput() == input)
                return recipe.getOutputValue();
        }
        return 0;
    }
}
