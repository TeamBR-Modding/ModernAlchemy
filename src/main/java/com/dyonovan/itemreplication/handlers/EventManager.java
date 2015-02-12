package com.dyonovan.itemreplication.handlers;

import com.dyonovan.itemreplication.events.OnBreakEvent;
import com.dyonovan.itemreplication.events.ToolTipEvent;
import net.minecraftforge.common.MinecraftForge;

public class EventManager {

    public static void init() {
        registerEvent(new ToolTipEvent());
        registerEvent(BucketHandler.INSTANCE);
        registerEvent(new OnBreakEvent());
    }

    private static void registerEvent(Object event) {
        MinecraftForge.EVENT_BUS.register(event);
    }
}
