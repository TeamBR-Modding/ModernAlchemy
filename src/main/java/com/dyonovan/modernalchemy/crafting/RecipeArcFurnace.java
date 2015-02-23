package com.dyonovan.modernalchemy.crafting;

import net.minecraft.item.Item;

public class RecipeArcFurnace {
    private Item item;
    private int outputValue;

    /**
     * An instance of an arc furnace recipe
     * @param input The input item {@link net.minecraft.item.Item}
     * @param value The amout of fluid produced (mb)
     */
    public RecipeArcFurnace(Item input, int value) {
        this.item = input;
        this.outputValue = value;
    }

    /**
     * Returns the input item assigned to this recipe
     * @return {@link net.minecraft.item.Item}
     */
    public Item getInput() {
        return item;
    }

    /**
     * Get the fluid output
     * @return Output in mb
     */
    public int getOutputValue() {
        return outputValue;
    }
}
