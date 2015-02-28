package com.dyonovan.modernalchemy.events;

import com.dyonovan.modernalchemy.gui.BaseGui;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import com.dyonovan.modernalchemy.manual.pages.GuiManual;
import com.dyonovan.modernalchemy.util.ReplicatorUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class ToolTipEvent {
    @SubscribeEvent
    public void onToolTip(ItemTooltipEvent event) {
        if(Minecraft.getMinecraft().currentScreen instanceof BaseGui && !(Minecraft.getMinecraft().currentScreen instanceof GuiManual)) {
            if (ReplicatorUtils.getValueForItem(event.itemStack) > 0) {
                event.toolTip.add(GuiHelper.GuiColor.YELLOW + "Replicator Value: " + ReplicatorUtils.getValueForItem(event.itemStack));
            }
        }
    }
}
