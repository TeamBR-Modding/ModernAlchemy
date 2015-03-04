package com.dyonovan.modernalchemy.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class RecipeAdvancedCrafter {

    private ItemStack outputItem;
    private ArrayList<ItemStack> itemArray;
    private int processTime;
    private int requiredMode;

    /**
     * An instance of an advanced crafter recipe
     * @param itemArray  The array of input items {@link net.minecraft.item.Item}
     * @param itemOutput The output item
     * @param processTime The total amount of ticks required to process
     * @param requiredMode COOK = 1, EXTRUDE = 2, BEND = 3
     */
    public RecipeAdvancedCrafter(ArrayList<ItemStack> itemArray, ItemStack itemOutput, int processTime, int requiredMode) {
        this.itemArray = itemArray;
        this.outputItem = itemOutput.copy();
        this.processTime = processTime;
        this.requiredMode = requiredMode;
    }

    /**
     * Returns the input item assigned to this recipe
     * @return {@link net.minecraft.item.Item}
     */
    public ArrayList<ItemStack> getInput() {
        return itemArray;
    }

    /**
     * Get the fluid output
     * @return Output in mb
     */
    public ItemStack getOutputItem() {
        return outputItem;
    }

    public int getProcessTime() { return processTime; }

    public int getRequiredMode() { return requiredMode; }

    public int getQtyOutput() { return outputItem.stackSize; }
}
