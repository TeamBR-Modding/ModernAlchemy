package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.items.ItemReplicatorMedium;
import com.dyonovan.modernalchemy.items.ItemLaserNode;
import com.dyonovan.modernalchemy.items.ItemPattern;
import com.dyonovan.modernalchemy.items.buckets.ItemBucketActinium;
import com.dyonovan.modernalchemy.items.buckets.ItemSlag;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ItemHandler {

    public static ItemPattern itemPattern;
    public static Item itemBucketActinium, itemReplicationMedium;
    public static Item laserNode, itemSlag;

    public static void init() {
        itemPattern = new ItemPattern();
        GameRegistry.registerItem(itemPattern, "pattern");

        //Actinium Bucket
        itemBucketActinium = new ItemBucketActinium(BlockHandler.blockFluidActinium);
        GameRegistry.registerItem(itemBucketActinium, "bucketActinium");
        FluidContainerRegistry.registerFluidContainer(new FluidStack(BlockHandler.fluidActinium, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(itemBucketActinium), new ItemStack(Items.bucket));

        //Item Cube
        itemReplicationMedium = new ItemReplicatorMedium();
        GameRegistry.registerItem(itemReplicationMedium, "itemReplicationMedium");

        //Item Cube
        itemSlag = new ItemSlag();
        GameRegistry.registerItem(itemSlag, "itemSlag");

        //Laser Node
        laserNode = new ItemLaserNode();
        GameRegistry.registerItem(laserNode, "laserNodeItem");
    }


}
