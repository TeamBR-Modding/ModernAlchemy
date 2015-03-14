package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.common.entities.EntityLaserNode;
import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.common.registry.EntityRegistry;

public class EntityHandler {
    public static void init() {
        EntityRegistry.registerModEntity(EntityLaserNode.class, Constants.MODID + ":laserNode", 1, ModernAlchemy.instance, 80, 3, true);
    }
}
