package com.dyonovan.modernalchemy.util;

import com.dyonovan.modernalchemy.crafting.OreDictStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

public class InventoryUtils {
    /**
     * A comparator for ItemStacks, handles if it is null
     */
    public static Comparator<Object> itemStackComparator =  new Comparator<Object>() {
        @Override
        public int compare(Object o1, Object o2) {
            if(o2 instanceof OreDictStack && o1 instanceof OreDictStack)
                return ((OreDictStack)o1).oreId.compareTo(((OreDictStack) o2).oreId);
            else if(o1 instanceof OreDictStack && o2 instanceof ItemStack)
                return ((OreDictStack)o1).compareTo((ItemStack)o2);
            else if(o2 instanceof OreDictStack && o1 instanceof ItemStack)
                return -((OreDictStack)o2).compareTo((ItemStack)o1);
            else if(o1 instanceof ItemStack && o2 instanceof ItemStack)
                return o1 == null && o2 == null ? 0 : o1 == null  ? 1 : o2 == null ? -1 : ((ItemStack)o1).getItem().getUnlocalizedName().compareTo(((ItemStack)o2).getItem().getUnlocalizedName());
            return 0;
        }
    };

    /**
     * Compares if {@link net.minecraft.item.ItemStack}s are equal. Taking into account null stacks
     * @param o1 Stack one
     * @param o2 Stack two
     * @return true if equal
     */
    public static boolean areStacksEqual(Object o1, Object o2) {
        if((o1 == null && o2 != null) || (o1 != null && o2 == null)) //One is not null and the other is
            return false;
        else if(o1 == null && o2 == null) //Both null (those are equal)
            return true;
        else if(o1 instanceof ItemStack && o2 instanceof ItemStack) //Two itemstacks
            return(((ItemStack)o1).getItem().getUnlocalizedName().equalsIgnoreCase(((ItemStack)o2).getItem().getUnlocalizedName()));
        else if(o1 instanceof OreDictStack && o2 instanceof OreDictStack) //Two orestacks
            return ((OreDictStack)o1).oreId.equalsIgnoreCase(((OreDictStack)o2).oreId) && ((((OreDictStack) o1).stackSize == 0 || ((OreDictStack) o2).stackSize == 0) || ((OreDictStack) o1).stackSize == ((OreDictStack) o2).stackSize);
        else
            return false; //Otherwise false
    }
}
