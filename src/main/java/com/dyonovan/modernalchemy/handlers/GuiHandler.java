package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.client.gui.config.GuiSuperTeslaCoilLinks;
import com.dyonovan.modernalchemy.client.gui.config.GuiTeslaCoilLinks;
import com.dyonovan.modernalchemy.client.notification.GuiNotificationConfig;
import com.dyonovan.modernalchemy.common.container.ContainerSuperTeslaCoilLinks;
import com.dyonovan.modernalchemy.common.container.ContainerTeslaCoilLinks;
import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileSuperTeslaCoil;
import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileTeslaCoil;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    public static final int NOTIFICATION_CONFIG_ID = 0;
    public static final int TESLA_COIL_LINKS_GUI_ID = 6;
    public static final int TESLA_SUPER_COIL_LINKS_GUI_ID = 5;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case TESLA_COIL_LINKS_GUI_ID :
                return new ContainerTeslaCoilLinks((TileTeslaCoil) world.getTileEntity(x, y, z));
            case TESLA_SUPER_COIL_LINKS_GUI_ID:
                return new ContainerSuperTeslaCoilLinks();
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case NOTIFICATION_CONFIG_ID :
                return new GuiNotificationConfig();
            case TESLA_COIL_LINKS_GUI_ID :
                return new GuiTeslaCoilLinks((TileTeslaCoil) world.getTileEntity(x, y, z));
            case TESLA_SUPER_COIL_LINKS_GUI_ID :
                return new GuiSuperTeslaCoilLinks((TileSuperTeslaCoil) world.getTileEntity(x, y, z));
        }
        return null;
    }
}
