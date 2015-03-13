package com.dyonovan.teambrcore;

import com.dyonovan.teambrcore.achievement.AchievementRegistry;
import com.dyonovan.teambrcore.helpers.KeyInputHelper;
import com.dyonovan.teambrcore.lib.Constants;
import com.dyonovan.teambrcore.managers.GuiManager;
import com.dyonovan.teambrcore.nei.INEICallback;
import com.dyonovan.teambrcore.notification.NotificationHelper;
import com.dyonovan.teambrcore.notification.NotificationKeyBinding;
import com.dyonovan.teambrcore.notification.NotificationTickHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

@Mod(name = Constants.MODNAME, modid = Constants.MODID, version = Constants.VERSION)

@SuppressWarnings("unused")
public class TeamBRCore {

    @Instance(Constants.MODID)
    public static TeamBRCore instance;

    public static INEICallback nei;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        if(event.getSide() == Side.CLIENT) {
            FMLCommonHandler.instance().bus().register(new NotificationTickHandler());
            NotificationHelper.notificationConfig = new Configuration(new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator  + "TeamBR" + File.separator + "NotificationsSettings.cfg"));
            NotificationHelper.notificationXPos = NotificationHelper.notificationConfig.getInt("notification xpos", "notifications", 1, 0, 2, "0: Left\n1: Center\n2: Right");
            NotificationHelper.notificationConfig.save();
        }
        AchievementRegistry.instance = new AchievementRegistry();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if(event.getSide() == Side.CLIENT)
        {
            NotificationKeyBinding.init();
        }
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiManager());
        FMLCommonHandler.instance().bus().register(new KeyInputHelper());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
