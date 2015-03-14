package com.dyonovan.modernalchemy.common.items;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraft.item.Item;

public class ItemReplicatorMedium extends Item {

    public ItemReplicatorMedium() {
        this.setUnlocalizedName(Constants.MODID + ":itemReplicationMedium");
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
        this.setTextureName(Constants.MODID + ":replication_medium");
        this.setMaxStackSize(64);
    }
}
