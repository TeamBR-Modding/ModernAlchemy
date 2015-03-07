package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.container.*;
import com.dyonovan.modernalchemy.gui.*;
import com.dyonovan.modernalchemy.manual.ManualRegistry;
import com.dyonovan.modernalchemy.manual.pages.ContainerPage;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.TileArcFurnaceCore;
import com.dyonovan.modernalchemy.tileentity.machines.TileAdvancedCrafter;
import com.dyonovan.modernalchemy.tileentity.machines.TileAmalgamator;
import com.dyonovan.modernalchemy.tileentity.machines.TileElectricBellows;
import com.dyonovan.modernalchemy.tileentity.machines.TilePatternRecorder;
import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorCPU;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    public static final int BLAST_FURNACE_GUI_ID = 0;
    public static final int TESLA_COIL_GUI_ID = 1;
    public static final int ELECTRIC_BELLOWS_GUI_ID = 2;
    public static final int PATTERN_RECORDER_GUI_ID = 3;
    public static final int AMALGAMATOR_GUI_ID = 4;
    public static final int REPLICATOR_CPU_GUI_ID = 5;
    public static final int TESLA_COIL_LINKS_GUI_ID = 6;
    public static final int MANUAL_GUI_ID = 7;
    public static final int ADVANCED_FURNACE_GUI_ID = 8;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case BLAST_FURNACE_GUI_ID :
                return new ContainerArcFurnace(player.inventory, (TileArcFurnaceCore) world.getTileEntity(x, y, z));
            case TESLA_COIL_GUI_ID :
                return new ContainerTeslaCoil((TileTeslaCoil) world.getTileEntity(x, y, z));
            case ELECTRIC_BELLOWS_GUI_ID:
                return new ContainerElectricBellows((TileElectricBellows) world.getTileEntity(x, y, z));
            case PATTERN_RECORDER_GUI_ID :
                return new ContainerPatternRecorder(player.inventory, (TilePatternRecorder) world.getTileEntity(x, y, z));
            case AMALGAMATOR_GUI_ID:
                return new ContainerAmalgamator(player.inventory, (TileAmalgamator) world.getTileEntity(x, y, z));
            case REPLICATOR_CPU_GUI_ID:
                return new ContainerReplicatorCpu(player.inventory, (TileReplicatorCPU) world.getTileEntity(x, y, z));
            case TESLA_COIL_LINKS_GUI_ID :
                return new ContainerTeslaCoilLinks((TileTeslaCoil) world.getTileEntity(x, y, z));
            case MANUAL_GUI_ID :
                return new ContainerPage();
            case ADVANCED_FURNACE_GUI_ID:
                return new ContainerAdvancedCrafter(player.inventory, (TileAdvancedCrafter) world.getTileEntity(x, y, z));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case BLAST_FURNACE_GUI_ID :
                return new GuiArcFurnace(player.inventory, (TileArcFurnaceCore) world.getTileEntity(x, y, z));
            case TESLA_COIL_GUI_ID :
                return new GuiTeslaCoil((TileTeslaCoil) world.getTileEntity(x, y, z));
            case ELECTRIC_BELLOWS_GUI_ID:
                return new GuiElectricBellows((TileElectricBellows) world.getTileEntity(x, y, z));
            case PATTERN_RECORDER_GUI_ID :
                return new GuiPatternRecorder(player.inventory, (TilePatternRecorder) world.getTileEntity(x, y, z));
            case AMALGAMATOR_GUI_ID:
                return new GuiAmalgamator(player.inventory, (TileAmalgamator) world.getTileEntity(x, y, z));
            case REPLICATOR_CPU_GUI_ID:
                return new GuiReplicatorCPU(player.inventory, (TileReplicatorCPU) world.getTileEntity(x, y, z));
            case TESLA_COIL_LINKS_GUI_ID :
                return new GuiTeslaCoilLinks((TileTeslaCoil) world.getTileEntity(x, y, z));
            case MANUAL_GUI_ID :
                return ManualRegistry.instance.getOpenPage();
            case ADVANCED_FURNACE_GUI_ID:
                return new GuiAdvancedCrafter(player.inventory, (TileAdvancedCrafter) world.getTileEntity(x, y, z));
        }
        return null;
    }
}
