package com.dyonovan.itemreplication.handlers;

import com.dyonovan.itemreplication.items.ItemPattern;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created by Tim on 2/5/2015.
 */
public class ItemHandler {

    private static ItemPattern itemPattern;

    public static void init() {
        itemPattern = new ItemPattern();
        GameRegistry.registerItem(itemPattern, "pattern");
    }


}
