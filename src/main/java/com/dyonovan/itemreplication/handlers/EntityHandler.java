package com.dyonovan.itemreplication.handlers;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.entities.EntityLaserNode;
import com.dyonovan.itemreplication.lib.Constants;
import cpw.mods.fml.common.registry.EntityRegistry;

public class EntityHandler {
    public static void init() {
        EntityRegistry.registerModEntity(EntityLaserNode.class, Constants.MODID + ":laserNode", 1, ItemReplication.instance, 80, 3, true);
    }
}
