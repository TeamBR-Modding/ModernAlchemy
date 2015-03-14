package com.dyonovan.modernalchemy.helpers;

import com.dyonovan.modernalchemy.client.notification.NotificationHelper;
import com.dyonovan.modernalchemy.client.notification.NotificationKeyBinding;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class KeyInputHelper {
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(NotificationKeyBinding.menu.isPressed())
            NotificationHelper.openConfigurationGui();
    }
}
