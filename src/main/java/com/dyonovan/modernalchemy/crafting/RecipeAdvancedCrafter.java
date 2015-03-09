package com.dyonovan.modernalchemy.crafting;

import com.dyonovan.modernalchemy.util.InventoryUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdvancedCrafter {

    private Object outputItem;
    private List<Object> itemArray;
    private int processTime;
    private int requiredMode;

    /**
     * An instance of an advanced crafter recipe
     * @param itemArray  The array of input items {@link net.minecraft.item.Item}
     * @param itemOutput The output item
     * @param processTime The total amount of ticks required to process
     * @param requiredMode ENRICH = 1, EXTRUDE = 2, BEND = 3
     */
    public RecipeAdvancedCrafter(List<Object> itemArray, Object itemOutput, int processTime, int requiredMode) {
        this.itemArray = itemArray;
        this.outputItem = itemOutput;
        this.processTime = processTime;
        this.requiredMode = requiredMode;
    }

    /**
     * Returns the input item assigned to this recipe
     * @return {@link net.minecraft.item.Item}
     */
    public List<Object> getInput() {
        return itemArray;
    }

    public ItemStack getConvertedStack(int i) {
        if(itemArray.get(i) instanceof ItemStack)
            return (ItemStack)itemArray.get(i);
        else if(itemArray.get(i) instanceof OreDictStack)
            return ((OreDictStack)itemArray.get(i)).getItemList().get(0);
        else
            return null;
    }

    /**
     * Get the output item
     * @return ItemStack or List of ItemStacks
     */
    public ItemStack getOutputItem() {
        if(outputItem instanceof ItemStack)
            return ((ItemStack)outputItem).copy();
        else if(outputItem instanceof OreDictStack)
            return ((OreDictStack)outputItem).getItemList().get(0);
        else
            return null;
    }

    /**
     * Check if our output is an oredictstack
     * @return true if it is
     */
    public boolean isOutputOreDict() {
        return outputItem instanceof OreDictStack;
    }

    /**
     * Get the stack of items associated with the ore dict
     * @return List of itemstacks with oredict tag
     */
    public List<ItemStack> getOreDictStacks() {
        if(isOutputOreDict()) {
            List<ItemStack> stack = new ArrayList<ItemStack>();
            for(ItemStack s : ((OreDictStack)outputItem).getItemList())
                stack.add(s);
            return stack;
        }
        return null;
    }

    /**
     * How long does this recipe take
     * @return process time in ticks
     */
    public int getProcessTime() { return processTime; }

    /**
     * What mode is required for this recipe
     * @return required mode
     */
    public int getRequiredMode() { return requiredMode; }

    /**
     * Get the size of the output stack
     * @return The size of the output
     */
    public int getQtyOutput() {
        if(outputItem instanceof ItemStack)
            return ((ItemStack)outputItem).stackSize;
        else if(outputItem instanceof OreDictStack)
            return ((OreDictStack)outputItem).stackSize;
        else
            return 0;
    }

    public boolean doesInputMatch(List<ItemStack> input) {
        for(int i = 0; i < itemArray.size(); i++) {
            Object obj = itemArray.get(i);
            if(obj instanceof ItemStack) {
                if(!InventoryUtils.areStacksEqual((ItemStack) obj, input.get(i)))
                    return false;
            }
            else if(obj instanceof OreDictStack) {
                if(!((OreDictStack)obj).isEqual(input.get(i)))
                    return false;
            }
        }
        return true;
    }
}
