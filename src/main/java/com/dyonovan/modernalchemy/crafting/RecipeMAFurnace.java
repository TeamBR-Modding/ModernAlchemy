package com.dyonovan.modernalchemy.crafting;

import net.minecraft.item.Item;

import java.util.ArrayList;

public class RecipeMAFurnace {

    private Item outputItem;
    private ArrayList<Item> itemArray;

    /**
     * An instance of an arc furnace recipe
     * @param itemArray  The array of input items {@link net.minecraft.item.Item}
     * @param itemOutput The output item
     */
    public RecipeMAFurnace(ArrayList<Item> itemArray, Item itemOutput) {
        this.itemArray = itemArray;
        this.outputItem = itemOutput;
    }

    /**
     * Returns the input item assigned to this recipe
     * @return {@link net.minecraft.item.Item}
     */
    public ArrayList<Item> getInput() {
        return itemArray;
    }

    /**
     * Get the fluid output
     * @return Output in mb
     */
    public Item getOutputItem() {
        return outputItem;
    }
}
