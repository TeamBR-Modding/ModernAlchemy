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
    public static ItemPattern itemReplicatorPattern;
    public static Item itemBucketActinium, itemReplicationMedium, itemPumpModule, itemRealClock, itemMemory;
    public static Item laserNode, itemSlag, itemWrench, itemActiniumDust, itemCopperWire, itemBlankPCB;
    public static Item itemCircuit, itemMachineFrame, itemEnergyAntenna, itemGraphene, itemSteelGear;
    public static Item manual, itemFaradayWire, itemSteelIngot, itemCopperIngot, itemSteelPlate, itemCapacator;
    public static Item itemCopperCoil, itemDenseCopperCoil, itemSteelTube, itemFaradayIngot, itemTransformer;

    public static void preInit() {
        //Laser Node
        registerItem(laserNode = new ItemLaserNode(), "laserNodeItem", null);

        //Manual
        registerItem(manual = new ItemManual(), "manual", null);

        //Tools
        registerItem(itemWrench = new ItemWrench(), "wrench", null);

        //Faraday Armor
        registerItem(faradayHelm = new ItemFaradayArmor("faraday_helm", ARMOR, "faraday", 0), "faradayHelm", null);
        registerItem(faradayChest = new ItemFaradayArmor("faraday_chest", ARMOR, "faraday", 1), "faradayChest", null);
        registerItem(faradayLeg = new ItemFaradayArmor("faraday_leg", ARMOR, "faraday", 2), "faradayLeg", null);
        registerItem(faradayBoots = new ItemFaradayArmor("faraday_boots", ARMOR, "faraday", 3), "faradayBoots", null);

        //Ingots and Dusts
        registerItem(itemActiniumDust = new ItemOreActinium(), "itemActiniumDust", null);
        registerItem(itemFaradayIngot = new ItemCrafting("itemFaradayIngot", 64), "itemFaradayIngot", null);

        //Fluid Buckets
        registerItem(itemBucketActinium = new ItemBucketActinium(BlockHandler.blockFluidActinium), "bucketActinium", null);
        FluidContainerRegistry.registerFluidContainer(new FluidStack(BlockHandler.fluidActinium,
                FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(itemBucketActinium), new ItemStack(Items.bucket));

        //Items
        registerItem(itemReplicatorPattern = new ItemPattern(), "itemReplicatorPattern", null);
        registerItem(itemSlag = new ItemSlag(), "itemSlag", null);
        registerItem(itemGraphene = new ItemCrafting("itemGraphene", 64), "itemGraphene", null);
        registerItem(itemCopperWire = new ItemCrafting("itemCopperWire", 64), "itemCopperWire", "wireCopper");
        registerItem(itemSteelPlate = new ItemCrafting("itemSteelPlate", 64), "itemSteelPlate", "plateSteel");
        registerItem(itemSteelGear = new ItemCrafting("itemSteelGear", 64), "itemSteelGear", "gearSteel");
        registerItem(itemSteelTube = new ItemCrafting("itemSteelTube", 16), "itemSteelTube", null);
        registerItem(itemRealClock = new ItemCrafting("itemRealClock", 64), "itemRealClock", null);
        registerItem(itemBlankPCB = new ItemCrafting("itemBlankPCB", 64), "itemBlankPCB", null);
        registerItem(itemCircuit = new ItemCrafting("itemCircuit", 64), "itemCircuit", "circuitAdvanced");
        registerItem(itemCapacator = new ItemCrafting("itemCapacator", 64), "itemCapacator", null);
        registerItem(itemMemory = new ItemCrafting("itemMemory", 64), "itemMemory", null);
        registerItem(itemCopperCoil = new ItemCrafting("itemCopperCoil", 64), "itemCopperCoil", null);
        registerItem(itemDenseCopperCoil = new ItemCrafting("itemDenseCopperCoil", 64), "itemDenseCopperCoil", null);
        registerItem(itemMachineFrame = new ItemCrafting("itemMachineFrame", 1), "itemMachineFrame", null);
        registerItem(itemPumpModule = new ItemCrafting("itemPumpModule", 64), "itemPumpModule", null);
        registerItem(itemEnergyAntenna = new ItemCrafting("itemEnergyAntenna", 64), "itemEnergyAntenna", null);
        registerItem(itemFaradayWire = new ItemCrafting("itemFaradayWire", 64), "itemFaradayWire", null);
        registerItem(itemReplicationMedium = new ItemReplicatorMedium(), "itemReplicationMedium", null);
        //registerItem(itemTransformer = new ItemCrafting("itemTransformer", 16), "itemTransformer", null);
    }

    public static void initCopper() {
        registerItem(itemCopperIngot = new ItemCrafting("itemCopperIngot", 64), "itemCopperIngot", "ingotCopper");
    }

    public static void registerItem(Item registerItem, String name, String oreDict) {
        GameRegistry.registerItem(registerItem, name);
        if(oreDict != null)
            OreDictionary.registerOre(oreDict, registerItem);
    }

    public static void initSteel() {
        registerItem(itemSteelIngot = new ItemCrafting("itemSteelIngot", 64), "itemSteelIngot", "ingotSteel");
    }
}
