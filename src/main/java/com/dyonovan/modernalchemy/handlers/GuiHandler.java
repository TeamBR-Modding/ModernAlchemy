package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.client.gui.machines.GuiElectricBellows;
import com.dyonovan.modernalchemy.client.gui.machines.GuiPatternRecorder;
import com.dyonovan.modernalchemy.client.gui.machines.GuiReplicatorCPU;
import com.dyonovan.modernalchemy.client.gui.machines.GuiTeslaCoil;
import com.dyonovan.modernalchemy.common.container.*;
import com.dyonovan.modernalchemy.client.gui.config.GuiTeslaCoilLinks;
import com.dyonovan.modernalchemy.client.manual.ManualRegistry;
import com.dyonovan.modernalchemy.client.manual.pages.ContainerPage;
import com.dyonovan.modernalchemy.common.container.machines.ContainerElectricBellows;
import com.dyonovan.modernalchemy.common.container.machines.ContainerPatternRecorder;
import com.dyonovan.modernalchemy.common.container.machines.ContainerReplicatorCpu;
import com.dyonovan.modernalchemy.common.tileentity.machines.TileElectricBellows;
import com.dyonovan.modernalchemy.common.tileentity.machines.TilePatternRecorder;
import com.dyonovan.modernalchemy.common.tileentity.replicator.TileReplicatorCPU;
import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.client.notification.GuiNotificationConfig;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    public static final int NOTIFICATION_CONFIG_ID = 0;
    public static final int REPLICATOR_CPU_GUI_ID = 5;
    public static final int TESLA_COIL_LINKS_GUI_ID = 6;
    public static final int MANUAL_GUI_ID = 7;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case REPLICATOR_CPU_GUI_ID:
                return new ContainerReplicatorCpu(player.inventory, (TileReplicatorCPU) world.getTileEntity(x, y, z));
            case TESLA_COIL_LINKS_GUI_ID :
                return new ContainerTeslaCoilLinks((TileTeslaCoil) world.getTileEntity(x, y, z));
            case MANUAL_GUI_ID :
                return new ContainerPage();
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case NOTIFICATION_CONFIG_ID :
                return new GuiNotificationConfig();
            case REPLICATOR_CPU_GUI_ID:
                return new GuiReplicatorCPU(player.inventory, (TileReplicatorCPU) world.getTileEntity(x, y, z));
            case TESLA_COIL_LINKS_GUI_ID :
                return new GuiTeslaCoilLinks((TileTeslaCoil) world.getTileEntity(x, y, z));
            case MANUAL_GUI_ID :
                return ManualRegistry.instance.getOpenPage();
        }
        return null;
    }
}
