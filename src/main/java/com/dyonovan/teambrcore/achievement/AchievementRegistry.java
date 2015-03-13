package com.dyonovan.teambrcore.achievement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

import java.util.HashMap;

public class AchievementRegistry {
    public static AchievementRegistry instance;

    private static HashMap<String, AchievementList> achievements;

    public AchievementRegistry() {
        achievements = new HashMap<String, AchievementList>();
    }

    public static void putAchievementList(AchievementList list) {
        achievements.put(list.getName(), list);
    }

    public static void registerModAchievements(String name, Achievement... achievements) {
        AchievementPage modpage = new AchievementPage(name, achievements);
        AchievementPage.registerAchievementPage(modpage);
    }

    public static void triggerAchievement(String modName, String achievementName, EntityPlayer player) {
        for(Achievement achieve : achievements.get(modName).getAchievements()) {
            if(achieve.statId.equalsIgnoreCase(achievementName)) {
                player.triggerAchievement(achieve);
                return;
            }
        }
    }
}
