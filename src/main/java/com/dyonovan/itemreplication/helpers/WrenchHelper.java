package com.dyonovan.itemreplication.helpers;

import cofh.api.item.IToolHammer;
import cpw.mods.fml.common.Loader;
import net.minecraft.item.Item;

public class WrenchHelper {

    public static boolean isWrench(Item item) {
        if(Loader.isModLoaded("BuildCraft|Core")) {
            if(item.getUnlocalizedName().equalsIgnoreCase("item.wrenchItem"))
                return true;
        }
        return (item instanceof IToolHammer);
    }
}
