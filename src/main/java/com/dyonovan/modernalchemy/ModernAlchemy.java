package com.dyonovan.modernalchemy;

import com.dyonovan.modernalchemy.achievement.ModAchievements;
import com.dyonovan.modernalchemy.handlers.*;
import com.dyonovan.modernalchemy.lib.Constants;
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
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import openmods.OpenMods;
import openmods.api.IProxy;
import openmods.config.game.ModStartupHelper;

import java.io.File;

@Mod(name = Constants.MODNAME, modid = Constants.MODID, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES)

public class ModernAlchemy {

    @Instance(Constants.MODID)
    public static ModernAlchemy instance;

    public static final DamageSource shock = new DamageSource("shock").setFireDamage().setDamageBypassesArmor();

    @SidedProxy(clientSide = "com.dyonovan." + Constants.MODID + ".proxy.ClientProxy",
            serverSide = "com.dyonovan." + Constants.MODID + ".proxy.CommonProxy")

    public static IProxy proxy;

    public static CreativeTabs tabModernAlchemy = new CreativeTabs("tabModernAlchemy") {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return Item.getItemFromBlock(BlockHandler.blockCoil);
        }
    };

    private final ModStartupHelper startupHelper = new ModStartupHelper(Constants.MODID);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){

        ConfigHandler.init(new Configuration(new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + Constants.MODID.toLowerCase() + File.separator + "general.properties")));
        BlockHandler.preInit();
        startupHelper.registerBlocksHolder(BlockHandler.class);
        startupHelper.preInit(event.getSuggestedConfigurationFile());
        ItemHandler.preInit();
        CraftingHandler.preInit();
        EntityHandler.init();
        EventManager.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, OpenMods.proxy.wrapHandler(new GuiHandler()));

        BucketHandler.INSTANCE.buckets.put(BlockHandler.blockFluidActinium, ItemHandler.itemBucketActinium);

        ReplicatorUtils.buildDirectory(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + Constants.MODID.toLowerCase() + File.separator + "replicatorValues");

        ModAchievements.instance = new ModAchievements();
        proxy.preInit();
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void init(FMLInitializationEvent event) {

        if (OreDictionary.getOres("oreCopper").isEmpty()) {
            BlockHandler.initCopper();
            ItemHandler.initCopper();
        }
        if (OreDictionary.getOres("ingotSteel").isEmpty()) {
            ItemHandler.initSteel();
        }

        WorldGeneratorHandler.init();
        CraftingHandler.init();
        PacketHandler.initPackets();
        FMLInterModComms.sendMessage("Waila", "register", "com.dyonovan.modernalchemy.waila.WailaDataProvider.callbackRegister");

        proxy.init();
        proxy.registerRenderInformation();
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
}
