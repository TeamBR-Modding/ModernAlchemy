package com.dyonovan.modernalchemy.helpers;

import net.minecraftforge.oredict.OreDictionary;

public class OreDictHelper {

    /**
     * Used to check if there is an ore dict stack for a string
     * @param string Ore Dict name
     * @return True if there is an itemstack registered
     */
    public static boolean hasOreDict(String string) {
        return !OreDictionary.getOres(string).isEmpty();
    }
}
