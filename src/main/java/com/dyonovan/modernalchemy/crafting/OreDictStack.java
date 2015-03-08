package com.dyonovan.modernalchemy.crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class OreDictStack {
    public String oreId;
    public int stackSize;

    /**
     * Creates an OreDictStack without caring about size (used most often)
     * @param id Ore Dict Name
     */
    public OreDictStack(String id) {
        this(id, 0);
    }

    /**
     * Creates an OreDictStack using the ore string
     * @param id The string used to register the stack wanted
     */
    public OreDictStack(String id, int size) {
        oreId = id;
        stackSize = size;
    }

    /**
     * Returns if the stack has items
     * @return
     */
    public boolean isValid() {
        return OreDictionary.getOres(oreId) != null;
    }

    /**
     * Returns if the given stack is equal to this stack
     * @param item Input ItemStack
     * @return true if equal
     */
    public boolean isEqual(ItemStack item) {
        if(isValid() && item != null) {
            for (ItemStack stack : OreDictionary.getOres(oreId)) {
                if(OreDictionary.itemMatches(stack, item, true))
                    return true;
            }
        }
        return false;
    }

    public int compareTo(ItemStack stack) {
        return oreId.compareTo(stack.getUnlocalizedName());
    }

    /**
     * Returns the list of items associated with this OreDictStack
     * @return
     */
    public List<ItemStack> getItemList() {
        List<ItemStack> output = new ArrayList<>();

        for(ItemStack stack : OreDictionary.getOres(oreId)) {
            if(stackSize == 0)
                output.add(new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
            else
                output.add(new ItemStack(stack.getItem(), stackSize, stack.getItemDamage()));
        }

        return output;
    }

    /**
     * Get the size of the stack
     * @return
     */
    public int getSize() {
        return stackSize;
    }

    /**
     * Converts the object to string
     * @return String representation of object
     */
    @Override
    public String toString() {
        return oreId + " : " + stackSize;
    }
}
