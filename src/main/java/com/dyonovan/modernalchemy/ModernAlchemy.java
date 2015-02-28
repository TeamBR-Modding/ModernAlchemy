package com.dyonovan.modernalchemy;

import com.dyonovan.modernalchemy.handlers.*;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.manual.ManualRegistry;
import com.dyonovan.modernalchemy.nei.INEICallback;
import com.dyonovan.modernalchemy.proxy.CommonProxy;
import com.dyonovan.modernalchemy.manual.ManualComponents;
import com.dyonovan.modernalchemy.manual.ManualJson;
import com.dyonovan.modernalchemy.util.ReplicatorUtils;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;

@Mod(name = Constants.MODNAME, modid = Constants.MODID, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES)

public class ModernAlchemy {

    @Instance(Constants.MODID)
    public static ModernAlchemy instance;

    public static INEICallback nei;

    @SidedProxy(clientSide = "com.dyonovan." + Constants.MODID + ".proxy.ClientProxy",
            serverSide = "com.dyonovan." + Constants.MODID + ".proxy.CommonProxy")
    public static CommonProxy proxy;

    public static CreativeTabs tabModernAlchemy = new CreativeTabs("tabModernAlchemy") {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return Item.getItemFromBlock(BlockHandler.blockCoil);
        }
    };

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        ConfigHandler.init(new Configuration(new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + Constants.MODID.toLowerCase() + File.separator + "general.properties")));
        BlockHandler.init();
        ItemHandler.init();
        CraftingHandler.init();
        EntityHandler.init();
        WorldGeneratorHandler.init();
        EventManager.init();
        proxy.registerRenderer();

        BucketHandler.INSTANCE.buckets.put(BlockHandler.blockFluidActinium, ItemHandler.itemBucketActinium);
        ManualRegistry.instance.init();

        ReplicatorUtils.buildDirectory(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + Constants.MODID.toLowerCase() + File.separator + "replicatorValues");

        //TESTING
        ArrayList<ManualComponents> comp = new  ArrayList<ManualComponents>();
        ArrayList<String> tools = new ArrayList<String>();
        tools.add("Test");
        ResourceLocation rl = new ResourceLocation(Constants.MODID + ":textures/gui/ma_furnace.png");
        comp.add(new ManualComponents(1, 2, 3, 4, 1, "Test", "", new ItemStack(Items.diamond), rl, tools));
        ArrayList<ManualJson> test = new ArrayList<ManualJson>();
        test.add(new ManualJson("Test1", 1, "test2", comp));

        ManualRegistry.instance.writeManJson(test);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        PacketHandler.initPackets();
        FMLInterModComms.sendMessage("Waila", "register", "com.dyonovan.modernalchemy.waila.WailaDataProvider.callbackRegister");
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {}
}
