package com.dyonovan.itemreplication.events;

import com.dyonovan.itemreplication.blocks.BlockBase;
import com.dyonovan.itemreplication.handlers.ConfigHandler;
import com.dyonovan.itemreplication.helpers.WrenchHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.BlockEvent;

public class OnBreakEvent {

    @SubscribeEvent
    public void onBreakEvent(BlockEvent.BreakEvent event) {

        if (event.block instanceof BlockBase && !WrenchHelper.isWrench(event.getPlayer().getCurrentEquippedItem().getItem()) &&
                ConfigHandler.machineExplodes) {
            event.world.createExplosion(event.getPlayer(), event.x, event.y, event.z, 2.0F, true);
        }
    }
}
