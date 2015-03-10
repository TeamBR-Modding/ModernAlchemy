package com.dyonovan.modernalchemy.achievement;

import com.dyonovan.modernalchemy.handlers.BlockHandler;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AchievementRegistry {
    public static Achievement mineActinium;

    public static void init() {
        mineActinium = new Achievement("achievement.mineActinium", "mineActiniumAchievement", 0, 0, BlockHandler.blockOreActinium, null).registerStat();

        AchievementPage page = new AchievementPage("Modern Alchemy", mineActinium);
        AchievementPage.registerAchievementPage(page);
    }
}
