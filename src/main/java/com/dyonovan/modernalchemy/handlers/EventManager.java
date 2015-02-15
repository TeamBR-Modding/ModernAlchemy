package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.events.OnBreakEvent;
import com.dyonovan.modernalchemy.events.ToolTipEvent;
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
