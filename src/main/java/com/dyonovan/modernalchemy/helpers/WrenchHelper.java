package com.dyonovan.modernalchemy.helpers;

import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import cpw.mods.fml.common.Loader;
import net.minecraft.item.Item;

public class WrenchHelper {

    /**
     * Test if item if a known mod wrench
     * @param item Wrench in question
     * @return true if it is a wrench that we like
     */
    public static boolean isWrench(Item item) {
        return (item instanceof IToolHammer) || (item instanceof IToolWrench);
    }
}
