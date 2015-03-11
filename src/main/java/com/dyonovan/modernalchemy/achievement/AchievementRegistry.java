package com.dyonovan.modernalchemy.achievement;

import com.dyonovan.modernalchemy.collections.AutoInit;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.teambrcore.achievement.AchievementList;

public class AchievementRegistry extends AchievementList{
    public static String mineActinium = "mineActinium";

    @AutoInit
    public AchievementRegistry() {
        super("Modern Alchemy");
        System.out.println("HELLO THERE");
    }

    @Override
    public void initAchievements() {
        buildAchievement("mineActinium", 0, 0, BlockHandler.blockOreActinium, null);
    }
}
