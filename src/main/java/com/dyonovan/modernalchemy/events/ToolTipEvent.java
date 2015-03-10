package com.dyonovan.modernalchemy.events;

import com.dyonovan.modernalchemy.items.ItemPattern;
import com.dyonovan.modernalchemy.manual.pages.GuiManual;
import com.dyonovan.modernalchemy.util.ReplicatorUtils;
import com.dyonovan.modernalchemy.util.ReplicatorValues;
import com.dyonovan.teambrcore.gui.BaseGui;
import com.dyonovan.teambrcore.helpers.GuiHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class ToolTipEvent {
    @SubscribeEvent
    public void onToolTip(ItemTooltipEvent event) {
        if(Minecraft.getMinecraft().currentScreen instanceof BaseGui && !(Minecraft.getMinecraft().currentScreen instanceof GuiManual) &&
                !(event.itemStack.getItem() instanceof ItemPattern)) {

            ReplicatorValues values = ReplicatorUtils.getValueForItem(event.itemStack);
            if (values != null) {

                event.toolTip.add(GuiHelper.GuiColor.YELLOW + "Replicator Value: " + values.reqTicks);
                event.toolTip.add(GuiHelper.GuiColor.YELLOW + "Quantity: " + values.qtyReturn);

            }
        }
    }
}
