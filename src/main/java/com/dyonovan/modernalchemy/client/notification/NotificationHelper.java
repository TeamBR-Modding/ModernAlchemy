package com.dyonovan.modernalchemy.client.notification;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;

public class NotificationHelper {

    public static Configuration notificationConfig;
    public static int notificationXPos;

    public static void addNotification(Notification notification) {
        NotificationTickHandler.guiNotification.queueNotification(notification);
    }

    public static void openConfigurationGui() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if(player != null)
            player.openGui(ModernAlchemy.instance, GuiHandler.NOTIFICATION_CONFIG_ID, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
    }

    public static void set(String categoryName, String propertyName, int newValue) {
        notificationConfig.load();
        if (notificationConfig.getCategoryNames().contains(categoryName)) {
            if (notificationConfig.getCategory(categoryName).containsKey(propertyName)) {
                notificationConfig.getCategory(categoryName).get(propertyName).set(newValue);
            }
        }
        notificationConfig.save();
        reloadValues();
    }

    public static void reloadValues() {
        notificationXPos = notificationConfig.getInt("notification xpos", "notifications", 1, 0, 2, "0: Left   1: Center   2: Right");
    }
}
