package com.dyonovan.modernalchemy.crafting;

import com.dyonovan.modernalchemy.helpers.LogHelper;
import com.dyonovan.modernalchemy.util.InventoryUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class AdvancedCrafterRecipeRegistry {

    /**
     * The instance of the recipies
     */
    public static AdvancedCrafterRecipeRegistry instance = new AdvancedCrafterRecipeRegistry();

    /**
     * The stored recipes
     */
    public List<RecipeAdvancedCrafter> recipes = new ArrayList<RecipeAdvancedCrafter>();

    /**
     * Adds a recipe to the registry if it doesn't already have a value
     *
     * @param itemArray  Input item array
     * @param output the Resulting Item
     */
    public void addRecipe(ArrayList<ItemStack> itemArray, ItemStack output, int processTime, int requiredMode) {
        Collections.sort(itemArray, InventoryUtils.itemStackComparator);

        if (!recipes.contains(new RecipeAdvancedCrafter(itemArray, output,  processTime, requiredMode)))
            recipes.add(new RecipeAdvancedCrafter(itemArray, output, processTime, requiredMode));
        else
            LogHelper.warning("Someone tried to add a recipe for " + output.getUnlocalizedName() + " when it already exists. Aborting.");
    }

    /**
     * Creates a recipe that can contain ore dict strings
     * @param itemArray Items and Strings for ore dict stuff
     * @param output The output {@link net.minecraft.item.Item}
     * @param processTime How long in ticks
     * @param requiredMode Which mode is needed
     */
    public void addOreDictRecipe(ArrayList<Object> itemArray, ItemStack output, int processTime, int requiredMode) {
        ArrayList<ItemStack> newInput = new ArrayList<ItemStack>();
        HashMap<String, List<ItemStack>> oreList = new HashMap<String, List<ItemStack>>();

        for(Object obj : itemArray) {
            if(obj instanceof ItemStack)
                newInput.add((ItemStack) obj);
            else if(obj instanceof Item)
                newInput.add(new ItemStack((Item) obj));
            else if(obj instanceof String) {
                if(OreDictionary.getOres((String) obj) != null) {
                    List<ItemStack> oreItems = new ArrayList<ItemStack>();
                    for(ItemStack stack : OreDictionary.getOres((String) obj)) {
                        if(OreDictionary.getOreIDs(stack).length > 1) {
                            System.out.println(stack.getDisplayName());
                            for(int oreID : OreDictionary.getOreIDs(stack)) {
                                if(oreID == OreDictionary.getOreID((String) obj))
                                    oreItems.add(stack);
                            }
                        }
                        else
                            oreItems.add(stack);
                    }
                    oreList.put((String) obj, oreItems);
                }
                else {
                    LogHelper.severe("No ore dictionary value exists for: " + (String) obj + ". Aborting");
                    return;
                }
            }

            for(String key : oreList.keySet()) {
                for(ItemStack item : oreList.get(key)) {
                    ArrayList<ItemStack> copyInput = new ArrayList<ItemStack>();
                    copyInput.addAll(newInput);
                    copyInput.add(item);
                    addRecipe(copyInput, output, processTime, requiredMode);
                }
            }
        }
    }

    /**
     * Check if item is valid for crafting
     * @param input The item to check {@link net.minecraft.item.Item}
     * @return True if it is
     */
    public boolean checkInput(Item input) {
        for(RecipeAdvancedCrafter recipe : recipes) {
            if (recipe.getInput().contains(input)) return true;
        }
        return false;
    }
}
