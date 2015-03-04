package com.dyonovan.modernalchemy.crafting;

import com.dyonovan.modernalchemy.helpers.LogHelper;
import com.dyonovan.modernalchemy.util.InventoryUtils;
import com.google.common.collect.Lists;
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
        List<Collection<ItemStack>> oreDictStacks = new ArrayList<Collection<ItemStack>>();

        for(Object obj : itemArray) {
            if (obj instanceof ItemStack)
                newInput.add((ItemStack) obj);
            else if (obj instanceof Item)
                newInput.add(new ItemStack((Item) obj));
            else if (obj instanceof String) {
                if (OreDictionary.getOres((String) obj) != null) {
                    List<ItemStack> oreItems = new ArrayList<ItemStack>();
                    for (ItemStack stack : OreDictionary.getOres((String) obj)) {
                        if (OreDictionary.getOreIDs(stack).length > 1) {
                            for (int oreID : OreDictionary.getOreIDs(stack)) {
                                if (oreID == OreDictionary.getOreID((String) obj))
                                    oreItems.add(stack);
                            }
                        } else
                            oreItems.add(stack);
                    }
                    oreDictStacks.add(oreItems);
                } else {
                    LogHelper.severe("No ore dictionary value exists for: " + (String) obj + ". Aborting");
                    return;
                }
            }
        }

        Collection<List<ItemStack>> permutated = permutations(oreDictStacks);

        for(List<ItemStack> list : permutated) {
            ArrayList<ItemStack> newList = new ArrayList<ItemStack>(list);
            addRecipe(newList, output, processTime, requiredMode);
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

    /**
     * Combines several collections of elements and create permutations of all of them, taking one element from each
     * collection, and keeping the same order in resultant lists as the one in original list of collections.
     *
     * <ul>Example
     * <li>Input  = { {a,b,c} , {1,2,3,4} }</li>
     * <li>Output = { {a,1} , {a,2} , {a,3} , {a,4} , {b,1} , {b,2} , {b,3} , {b,4} , {c,1} , {c,2} , {c,3} , {c,4} }</li>
     * </ul>
     *
     * @param collections Original list of collections which elements have to be combined.
     * @return Resultant collection of lists with all permutations of original list.
     */
    public static <T> Collection<List<T>> permutations(List<Collection<T>> collections) {
        if (collections == null || collections.isEmpty()) {
            return Collections.emptyList();
        } else {
            Collection<List<T>> res = Lists.newLinkedList();
            permutationsImpl(collections, res, 0, new LinkedList<T>());
            return res;
        }
    }

    private static <T> void permutationsImpl(List<Collection<T>> ori, Collection<List<T>> res, int d, List<T> current) {
        // if depth equals number of original collections, final reached, add and return
        if (d == ori.size()) {
            res.add(current);
            return;
        }

        // iterate from current collection and copy 'current' element N times, one for each element
        Collection<T> currentCollection = ori.get(d);
        for (T element : currentCollection) {
            List<T> copy = Lists.newLinkedList(current);
            copy.add(element);
            permutationsImpl(ori, res, d + 1, copy);
        }
    }
}
