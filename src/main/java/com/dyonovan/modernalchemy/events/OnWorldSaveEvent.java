package com.dyonovan.modernalchemy.events;

import com.dyonovan.modernalchemy.util.JsonUtils;
import com.dyonovan.modernalchemy.util.ReplicatorUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;
import scala.tools.nsc.interactive.REPL;

public class OnWorldSaveEvent {

    @SubscribeEvent
    public void OnWorldSaveEvent(WorldEvent.Save event) {
        if (!ReplicatorUtils.values.isEmpty())
        JsonUtils.writeJson(ReplicatorUtils.values, "zzzDynamic");
    }
}
