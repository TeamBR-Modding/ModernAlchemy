package com.dyonovan.modernalchemy.crafting;

import com.dyonovan.modernalchemy.collections.Couple;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

public class CraftingRecipeHelper {

    public static Couple<ItemStack[], IRecipe> getRecipe(ItemStack item) {
        List<IRecipe> registeredRecipes = CraftingManager.getInstance().getRecipeList();
        ItemStack[] outputStack = new ItemStack[9];

        for(IRecipe recipe : registeredRecipes) {
            ItemStack outputCheck = recipe.getRecipeOutput();
            if(outputCheck != null) {
                if(outputCheck.getItem() == item.getItem()) {
                    if(recipe instanceof ShapedRecipes) {
                        for(int i = 0; i < ((ShapedRecipes) recipe).recipeItems.length; i++) {
                            outputStack[i] = ((ShapedRecipes) recipe).recipeItems[i];
                        }
                        return new Couple(outputStack, recipe);
                    }
                    else if(recipe instanceof ShapelessRecipes) {
                        for(int i = 0; i < ((ShapelessRecipes) recipe).recipeItems.size(); i++) {
                            outputStack[i] = (ItemStack) ((ShapelessRecipes) recipe).recipeItems.get(i);
                        }
                        return new Couple(outputStack, recipe);
                    }
                    else if(recipe instanceof ShapedOreRecipe) {
                        for(int i = 0; i < ((ShapedOreRecipe)recipe).getInput().length; i++) {
                            if(((ShapedOreRecipe)recipe).getInput()[i] instanceof ItemStack) {
                                outputStack[i] = (ItemStack) ((ShapedOreRecipe)recipe).getInput()[i];
                            }
                            else if(((ShapedOreRecipe)recipe).getInput()[i] instanceof ArrayList) {
                                Object o = ((ArrayList<?>)((ShapedOreRecipe)recipe).getInput()[i]).get(0);
                                if(o instanceof ItemStack)
                                    outputStack[i] = (ItemStack)o;
                            }
                        }
                        return new Couple(outputStack, recipe);
                    }
                    else if(recipe instanceof ShapelessOreRecipe) {
                        for(int i = 0; i < ((ShapelessOreRecipe)recipe).getInput().size(); i++) {
                            if(((ShapelessOreRecipe)recipe).getInput().get(i) instanceof ItemStack)
                                outputStack[i] = (ItemStack) ((ShapelessOreRecipe)recipe).getInput().get(i);
                            else if(((ShapelessOreRecipe)recipe).getInput().get(i) instanceof ArrayList) {
                                Object o = ((ArrayList<?>)((ShapelessOreRecipe)recipe).getInput().get(i)).get(0);
                                if(o instanceof ItemStack)
                                    outputStack[i] = (ItemStack) o;
                            }
                        }
                        return new Couple(outputStack, recipe);
                    }

                }
            }
        }
        return null;
    }
}
