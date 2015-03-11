package com.dyonovan.modernalchemy.achievement;

import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.teambrcore.achievement.AchievementList;

public class ModAchievements extends AchievementList {
    public static ModAchievements instance;
    public static String ModName = "Modern Alchemy";
    public static String mineActinium = "mineActinium";

    public ModAchievements() {
        super(ModName);
    }

    @Override
    public void initAchievements() {
        buildAchievement(mineActinium, 0, 0, BlockHandler.blockOreActinium, null);
    }
}
