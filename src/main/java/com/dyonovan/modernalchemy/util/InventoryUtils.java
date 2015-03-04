package com.dyonovan.modernalchemy.util;

import net.minecraft.item.ItemStack;

import java.util.Comparator;

public class InventoryUtils {
    /**
     * A comparator for ItemStacks, handles if it is null
     */
    public static Comparator<ItemStack> itemStackComparator =  new Comparator<ItemStack>() {
        @Override
        public int compare(ItemStack o1, ItemStack o2) {
            return o1 == null && o2 == null ? 0 : o1 == null  ? 1 : o2 == null ? -1 : o1.getItem().getUnlocalizedName().compareTo(o2.getItem().getUnlocalizedName());
        }
    };

    /**
     * Compares if {@link net.minecraft.item.ItemStack}s are equal. Taking into account null stacks
     * @param o1 Stack one
     * @param o2 Stack two
     * @return true if equal
     */
    public static boolean areStacksEqual(ItemStack o1, ItemStack o2) {
        return (o1 == null || o2 == null) || (o1.getItem().getUnlocalizedName().equalsIgnoreCase(o2.getItem().getUnlocalizedName()));
    }
}
