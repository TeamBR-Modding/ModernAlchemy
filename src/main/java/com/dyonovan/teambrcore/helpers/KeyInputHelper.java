package com.dyonovan.teambrcore.helpers;

import com.dyonovan.teambrcore.notification.NotificationHelper;
import com.dyonovan.teambrcore.notification.NotificationKeyBinding;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class KeyInputHelper {
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(NotificationKeyBinding.menu.isPressed())
            NotificationHelper.openConfigurationGui();
    }
}
