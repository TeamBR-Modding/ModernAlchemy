package com.dyonovan.itemreplication.handlers;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    public static void init(Configuration config) {

        config.load();

        config.save();
    }
}
