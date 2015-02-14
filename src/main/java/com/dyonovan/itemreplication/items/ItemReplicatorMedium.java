package com.dyonovan.itemreplication.items;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.lib.Constants;
import net.minecraft.item.Item;

public class ItemReplicatorMedium extends Item {

    public ItemReplicatorMedium() {
        this.setUnlocalizedName(Constants.MODID + ":itemReplicationMedium");
        this.setCreativeTab(ItemReplication.tabItemReplication);
        this.setTextureName(Constants.MODID + ":cube");
        this.setMaxStackSize(64);
    }
}
