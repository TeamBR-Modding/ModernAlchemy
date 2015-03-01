package com.dyonovan.modernalchemy.crafting;

import net.minecraft.item.Item;

import java.util.ArrayList;

public class RecipeAdvancedCrafter {

    private Item outputItem;
    private ArrayList<Item> itemArray;
    private int processTime;
    private int requiredMode;
    private int qty;

    /**
     * An instance of an advanced crafter recipe
     * @param itemArray  The array of input items {@link net.minecraft.item.Item}
     * @param itemOutput The output item
     * @param qty Qty of Output Item
     * @param processTime The total amount of ticks required to process
     * @param requiredMode COOK = 1, EXTRUDE = 2, BEND = 3
     */
    public RecipeAdvancedCrafter(ArrayList<Item> itemArray, Item itemOutput, int qty, int processTime, int requiredMode) {
        this.itemArray = itemArray;
        this.outputItem = itemOutput;
        this.processTime = processTime;
        this.requiredMode = requiredMode;
        this.qty = qty;
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

    public int getProcessTime() { return processTime; }

    public int getRequiredMode() { return requiredMode; }

    public int getQtyOutput() { return qty; }
}
