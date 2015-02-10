package com.dyonovan.itemreplication.items;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.lib.Constants;
import net.minecraft.item.Item;

public class ItemCube extends Item {

    public ItemCube() {
        this.setUnlocalizedName(Constants.MODID + ":itemCube");
        this.setCreativeTab(ItemReplication.tabItemReplication);
        this.setTextureName(Constants.MODID + ":cube");
        this.setMaxStackSize(64);
    }
}
