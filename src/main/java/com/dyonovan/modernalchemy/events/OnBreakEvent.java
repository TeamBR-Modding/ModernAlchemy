package com.dyonovan.modernalchemy.events;

import com.dyonovan.modernalchemy.achievement.ModAchievements;
import com.dyonovan.modernalchemy.blocks.BlockBase;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.helpers.WrenchHelper;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.teambrcore.achievement.AchievementRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.BlockEvent;

public class OnBreakEvent {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onBreakEvent(BlockEvent.BreakEvent event) {

        if (event.block instanceof BlockBase && ConfigHandler.machineExplodes && !event.getPlayer().capabilities.isCreativeMode) {
            if (event.getPlayer().getCurrentEquippedItem() != null) {
                if (WrenchHelper.isWrench(event.getPlayer().getCurrentEquippedItem().getItem())) {
                    return;
                }
            }
        } else if(event.block == BlockHandler.blockOreActinium && !event.getPlayer().capabilities.isCreativeMode) {
            AchievementRegistry.triggerAchievement(ModAchievements.ModName, ModAchievements.mineActinium, event.getPlayer());
            return;
        }
        else return;
        event.world.createExplosion(event.getPlayer(), event.x, event.y, event.z, 2.0F, true);
    }
}
