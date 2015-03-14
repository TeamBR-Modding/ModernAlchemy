package com.dyonovan.modernalchemy;

import com.dyonovan.modernalchemy.client.achievement.AchievementRegistry;
import com.dyonovan.modernalchemy.client.achievement.ModAchievements;
import com.dyonovan.modernalchemy.client.nei.INEICallback;
import com.dyonovan.modernalchemy.handlers.*;
import com.dyonovan.modernalchemy.helpers.KeyInputHelper;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.client.rpc.ILevelChanger;
import com.dyonovan.modernalchemy.util.ReplicatorUtils;
import com.dyonovan.modernalchemy.client.notification.NotificationHelper;
import com.dyonovan.modernalchemy.client.notification.NotificationKeyBinding;
import com.dyonovan.modernalchemy.client.notification.NotificationTickHandler;
import cpw.mods.fml.common.FMLCommonHandler;
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
import openmods.network.rpc.RpcCallDispatcher;

import java.io.File;

@Mod(name = Constants.MODNAME, modid = Constants.MODID, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES)

public class ModernAlchemy {

    @Instance(Constants.MODID)
    public static ModernAlchemy instance;

    public static final DamageSource shock = new DamageSource("shock").setFireDamage().setDamageBypassesArmor();

    @SidedProxy(clientSide = "com.dyonovan." + Constants.MODID + ".client.ClientProxy",
            serverSide = "com.dyonovan." + Constants.MODID + ".common.CommonProxy")
    public static IProxy proxy;

    public static INEICallback nei;

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

        if(event.getSide() == Side.CLIENT) {
            FMLCommonHandler.instance().bus().register(new NotificationTickHandler());
            NotificationHelper.notificationConfig = new Configuration(new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator  + "TeamBR" + File.separator + "NotificationsSettings.cfg"));
            NotificationHelper.notificationXPos = NotificationHelper.notificationConfig.getInt("notification xpos", "notifications", 1, 0, 2, "0: Left\n1: Center\n2: Right");
            NotificationHelper.notificationConfig.save();
        }
        AchievementRegistry.instance = new AchievementRegistry();

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

        RpcCallDispatcher.INSTANCE.startRegistration().registerInterface(ILevelChanger.class);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void init(FMLInitializationEvent event) {

        if(event.getSide() == Side.CLIENT)
        {
            NotificationKeyBinding.init();
        }
        FMLCommonHandler.instance().bus().register(new KeyInputHelper());

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
        FMLInterModComms.sendMessage("Waila", "register", "com.dyonovan.modernalchemy.client.waila.WailaDataProvider.callbackRegister");

        proxy.init();
        proxy.registerRenderInformation();
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
}
