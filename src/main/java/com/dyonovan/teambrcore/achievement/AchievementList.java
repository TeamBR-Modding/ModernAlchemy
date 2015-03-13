package com.dyonovan.teambrcore.achievement;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

import java.util.ArrayList;
import java.util.List;

public abstract class AchievementList {
    private String label;
    protected List<Achievement> achievements;

    public AchievementList(String name) {
        achievements = new ArrayList<>();
        label = name;
        AchievementRegistry.putAchievementList(this);
        initAchievements();
        AchievementRegistry.registerModAchievements(label, achievements.toArray(new Achievement[achievements.size()]));
    }

    public String getName() {
        return label;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void buildAchievement(String id, int x, int y, Item stack, Achievement parent) {
        achievements.add(new Achievement(id, id, x, y, stack, parent).registerStat());
    }

    public void buildAchievement(String id, int x, int y, Block stack, Achievement parent) {
        achievements.add(new Achievement(id, id, x, y, stack, parent).registerStat());
    }

    public void buildAchievement(String id, int x, int y, ItemStack stack, Achievement parent) {
        achievements.add(new Achievement(id, id, x, y, stack, parent).registerStat());
    }

    public abstract void initAchievements();
}
