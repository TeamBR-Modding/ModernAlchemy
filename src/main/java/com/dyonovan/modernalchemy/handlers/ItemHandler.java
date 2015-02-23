package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.items.*;
import com.dyonovan.modernalchemy.items.buckets.ItemBucketActinium;
import com.dyonovan.modernalchemy.manual.ItemManual;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemHandler {

    public static ItemArmor.ArmorMaterial ARMOR = EnumHelper.addArmorMaterial("FARADAY", 15, new int[]{2, 5, 4, 1}, 12);

    public static Item faradayHelm, faradayChest, faradayLeg, faradayBoots;
    public static ItemPattern itemPattern;
    public static Item itemBucketActinium, itemReplicationMedium;
    public static Item laserNode, itemSlag, itemWrench, itemActinium;
    public static Item itemCircuit, itemMachineFrame;
    public static Item manual;

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

        //Item Slag
        itemSlag = new ItemSlag();
        GameRegistry.registerItem(itemSlag, "itemSlag");

        //Laser Node
        laserNode = new ItemLaserNode();
        GameRegistry.registerItem(laserNode, "laserNodeItem");

        //itemWrench
        itemWrench = new ItemWrench();
        GameRegistry.registerItem(itemWrench, "wrench");

        //itemActinium
        itemActinium = new ItemOreActinium();
        GameRegistry.registerItem(itemActinium, "itemActinium");

        //Faraday Armor
        GameRegistry.registerItem(faradayHelm = new ItemFaradayArmor("faraday_helm", ARMOR, "faraday", 0), "faradayHelm");
        GameRegistry.registerItem(faradayChest = new ItemFaradayArmor("faraday_chest", ARMOR, "faraday", 1), "faradayChest");
        GameRegistry.registerItem(faradayLeg = new ItemFaradayArmor("faraday_leg", ARMOR, "faraday", 2), "faradayLeg");
        GameRegistry.registerItem(faradayBoots = new ItemFaradayArmor("faraday_boots", ARMOR, "faraday", 3), "faradayBoots");

        //Manual
        GameRegistry.registerItem(manual = new ItemManual(), "manual");

        //Crafting Items
        itemCircuit = new ItemCrafting("itemCircuit", 64);
        GameRegistry.registerItem(itemCircuit, "itemCircuit");
        OreDictionary.registerOre("circuitBasic", new ItemStack(itemCircuit));

        //Machine Frame
        itemMachineFrame = new ItemCrafting("itemMachineFrame", 1);
        GameRegistry.registerItem(itemMachineFrame, "itemMachineFrame");
    }


}
