package com.dyonovan.modernalchemy.items.buckets;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;

public class ItemBucketActinium extends ItemBucket {
    public ItemBucketActinium(Block block) {
        super(block);
        setUnlocalizedName(Constants.MODID + ":bucketActinium").setContainerItem(Items.bucket);
        setCreativeTab(ModernAlchemy.tabItemReplication);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon(Constants.MODID + ":bucket_actinium");
    }
}
