package com.dyonovan.itemreplication.tileentity;

import com.dyonovan.itemreplication.energy.ITeslaWorker;
import com.dyonovan.itemreplication.energy.TeslaMachine;
import com.dyonovan.itemreplication.handlers.ItemHandler;
import com.dyonovan.itemreplication.items.ItemPattern;
import net.minecraft.item.ItemStack;

/**
 * Created by Tim on 2/5/2015.
 */
public class TilePatternRecorder extends BaseTile implements ITeslaWorker {

    public ItemStack inventory[];
    private static final int ITEM_SLOT = 0;
    private static final int PATTERN_INPUT_SLOT = 1;
    private static final int PATTERN_OUTPUT_SLOT = 2;

    private TeslaMachine teslaMachine;

    public TilePatternRecorder() {
        inventory = new ItemStack[3];
        teslaMachine = new TeslaMachine(this);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        teslaMachine.update(worldObj, this);
    }

    @Override
    public boolean machineCanOperate() {
        return true; // no special conditions here
    }

    @Override
    public boolean canStartWork() {
        return inventory[PATTERN_INPUT_SLOT].getItem() == ItemHandler.itemPattern &&  // must have a pattern - doesn't matter if it is already recorded
                inventory[PATTERN_OUTPUT_SLOT].getItem() == null &&  // output must be empty
                inventory[ITEM_SLOT].getItem() != null; // must have an item - TODO: restrict items?
    }

    @Override
    public void onWorkStart() {
        // consume resources
        inventory[PATTERN_INPUT_SLOT].stackSize--;
        if(inventory[PATTERN_INPUT_SLOT].stackSize == 0)
            inventory[PATTERN_INPUT_SLOT] = null;
    }

    @Override
    public boolean doWork(int amount, int progress, int totalProcessTime) {
        return true;
    }

    @Override
    public void onWorkFinish() {
        // create products
        inventory[PATTERN_OUTPUT_SLOT] = new ItemStack(ItemHandler.itemPattern);
        ItemPattern.recordPattern(inventory[PATTERN_OUTPUT_SLOT], inventory[ITEM_SLOT]);
    }
}
