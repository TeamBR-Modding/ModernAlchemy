package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.common.items.*;
import com.dyonovan.modernalchemy.common.items.buckets.ItemBucketActinium;
import com.dyonovan.modernalchemy.common.items.ItemManual;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import openmods.config.ItemInstances;
import openmods.config.game.RegisterItem;

public class ItemHandler implements ItemInstances {

    public static ItemArmor.ArmorMaterial ARMOR = EnumHelper.addArmorMaterial("FARADAY", 15, new int[]{2, 5, 4, 1}, 12);

    public static Item faradayHelm, faradayChest, faradayLeg, faradayBoots;
    public static Item itemBucketActinium, itemPumpModule, itemRealClock, itemMemory;
    public static Item itemSlag, itemCopperWire, itemBlankPCB, itemFaradayIngot;
    public static Item itemCircuit, itemMachineFrame, itemEnergyAntenna, itemGraphene, itemSteelGear;
    public static Item itemFaradayWire, itemSteelIngot, itemCopperIngot, itemSteelPlate, itemCapacator;
    public static Item itemCopperCoil, itemDenseCopperCoil, itemSteelTube, itemTransformer;

    @RegisterItem(name = "manual")
    public static ItemManual manual = new ItemManual();

    @RegisterItem(name = "wrench")
    public static ItemWrench itemWrench = new ItemWrench();

    @RegisterItem(name = "itemActiniumDust")
    public static ItemOreActinium itemActiniumDust = new ItemOreActinium();

    @RegisterItem(name = "itemReplicatorPattern")
    public static ItemPattern itemReplicatorPattern = new ItemPattern();

    @RegisterItem(name = "itemReplicationMedium")
    public static ItemReplicatorMedium itemReplicationMedium = new ItemReplicatorMedium();

    public static void preInit() {
        //Fluid Buckets
        registerItem(itemBucketActinium = new ItemBucketActinium(BlockHandler.blockFluidActinium), "bucketActinium", null);
        FluidContainerRegistry.registerFluidContainer(new FluidStack(BlockHandler.fluidActinium,
                FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(itemBucketActinium), new ItemStack(Items.bucket));

        //Faraday Armor
        registerItem(faradayHelm = new ItemFaradayArmor("faradayHelm", ARMOR, "faraday", 0), "faradayHelm", null);
        registerItem(faradayChest = new ItemFaradayArmor("faradayChest", ARMOR, "faraday", 1), "faradayChest", null);
        registerItem(faradayLeg = new ItemFaradayArmor("faradayLeg", ARMOR, "faraday", 2), "faradayLeg", null);
        registerItem(faradayBoots = new ItemFaradayArmor("faradayBoots", ARMOR, "faraday", 3), "faradayBoots", null);


        //Items
        registerItem(itemSlag = new ItemSlag(), "itemSlag", null);
        registerItem(itemFaradayIngot = new ItemCrafting("itemFaradayIngot", 64), "itemFaradayIngot", null);
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
        //registerItem(itemTransformer = new ItemCrafting("itemTransformer", 16), "itemTransformer", null);
    }

    public static void initCopper() {
        registerItem(itemCopperIngot = new ItemCrafting("itemCopperIngot", 64), "itemCopperIngot", "ingotCopper");
    }

    public static void initSteel() {
        registerItem(itemSteelIngot = new ItemCrafting("itemSteelIngot", 64), "itemSteelIngot", "ingotSteel");
    }

    public static void registerItem(Item registerItem, String name, String oreDict) {
        GameRegistry.registerItem(registerItem, name);
        if(oreDict != null)
            OreDictionary.registerOre(oreDict, registerItem);
    }
}
