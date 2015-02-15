package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.ItemReplication;
import com.dyonovan.modernalchemy.entities.EntityLaserNode;
import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.common.registry.EntityRegistry;

public class EntityHandler {
    public static void init() {
        EntityRegistry.registerModEntity(EntityLaserNode.class, Constants.MODID + ":laserNode", 1, ItemReplication.instance, 80, 3, true);
    }
}
