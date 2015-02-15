package com.dyonovan.modernalchemy.items;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraft.item.Item;

public class ItemReplicatorMedium extends Item {

    public ItemReplicatorMedium() {
        this.setUnlocalizedName(Constants.MODID + ":itemReplicationMedium");
        this.setCreativeTab(ModernAlchemy.tabItemReplication);
        this.setTextureName(Constants.MODID + ":cube");
        this.setMaxStackSize(64);
    }
}
