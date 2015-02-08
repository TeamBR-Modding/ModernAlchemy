package com.dyonovan.itemreplication.tileentity;

import com.dyonovan.itemreplication.energy.TeslaBank;
import com.dyonovan.itemreplication.handlers.ItemHandler;
import com.dyonovan.itemreplication.items.ItemPattern;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Tim on 2/5/2015.
 */
public class TilePatternRecorder extends TileTeslaMachine {

    public ItemStack inventory[];
    private static final int ITEM_SLOT = 0;
    private static final int PATTERN_INPUT_SLOT = 1;
    private static final int PATTERN_OUTPUT_SLOT = 2;

    @Override
    protected void consumeResources() {
        inventory[PATTERN_INPUT_SLOT].stackSize--;
        if(inventory[PATTERN_INPUT_SLOT].stackSize == 0)
            inventory[PATTERN_INPUT_SLOT] = null;
    }

    @Override
    protected void createProducts() {
        inventory[PATTERN_OUTPUT_SLOT] = new ItemStack(ItemHandler.itemPattern);
        ItemPattern.recordPattern(inventory[PATTERN_OUTPUT_SLOT], inventory[ITEM_SLOT]);
    }

    @Override
    protected boolean resourcesAvailable() {
        return inventory[PATTERN_INPUT_SLOT].getItem() == ItemHandler.itemPattern &&  // must have a pattern - doesn't matter if it is already recorded
                inventory[PATTERN_OUTPUT_SLOT].getItem() == null &&  // output must be empty
                inventory[ITEM_SLOT].getItem() != null; // must have an item - TODO: restrict items?
    }

    @Override
    protected boolean canOperate() {
        return true;  // no special logic here
    }
}
