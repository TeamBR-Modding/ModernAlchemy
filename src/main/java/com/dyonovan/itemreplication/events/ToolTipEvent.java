package com.dyonovan.itemreplication.events;

import com.dyonovan.itemreplication.gui.BaseGui;
import com.dyonovan.itemreplication.helpers.GuiHelper;
import com.dyonovan.itemreplication.util.ReplicatorUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class ToolTipEvent {
    @SubscribeEvent
    public void onToolTip(ItemTooltipEvent event) {
        if(Minecraft.getMinecraft().currentScreen instanceof BaseGui) {
            if (ReplicatorUtils.getValueForItem(event.itemStack) > 0) {
                event.toolTip.add(GuiHelper.GuiColor.YELLOW + "Replicator Value: " + ReplicatorUtils.getValueForItem(event.itemStack));
            }
        }
    }
}
