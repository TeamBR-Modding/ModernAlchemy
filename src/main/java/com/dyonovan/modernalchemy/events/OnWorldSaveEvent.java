package com.dyonovan.modernalchemy.events;

import com.dyonovan.modernalchemy.util.JsonUtils;
import com.dyonovan.modernalchemy.util.ReplicatorUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;

public class OnWorldSaveEvent {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onWorldSaveEvent(WorldEvent.Save event) {
        if (!ReplicatorUtils.values.isEmpty())
        JsonUtils.writeJson(ReplicatorUtils.values, "zzzDynamic");
    }
}
