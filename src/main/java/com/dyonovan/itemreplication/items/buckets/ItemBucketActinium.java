package com.dyonovan.itemreplication.items.buckets;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;

public class ItemBucketActinium extends ItemBucket {
    public ItemBucketActinium(Block block) {
        super(block);
        setUnlocalizedName("bucketActinium").setContainerItem(Items.bucket);
        setCreativeTab(ItemReplication.tabItemReplication);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon(Constants.MODID + ":bucket_actinium");
    }
}
