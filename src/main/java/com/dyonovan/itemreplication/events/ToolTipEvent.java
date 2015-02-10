package com.dyonovan.itemreplication.events;

import com.dyonovan.itemreplication.helpers.GuiHelper;
import com.dyonovan.itemreplication.util.ReplicatorUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class ToolTipEvent {
    @SubscribeEvent
    public void onToolTip(ItemTooltipEvent event) {
        if(ReplicatorUtils.getValueForItem(event.itemStack) > 0) {
            event.toolTip.add(GuiHelper.GuiColor.YELLOW + "Replicator Value: " + ReplicatorUtils.getValueForItem(event.itemStack));
        }
    }
}
