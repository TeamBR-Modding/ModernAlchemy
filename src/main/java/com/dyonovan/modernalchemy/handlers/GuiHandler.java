package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.container.*;
import com.dyonovan.modernalchemy.gui.*;
import com.dyonovan.modernalchemy.manual.page.ContainerPage;
import com.dyonovan.modernalchemy.manual.page.MainPage;
import com.dyonovan.modernalchemy.manual.page.ManualPages;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.TileArcFurnaceCore;
import com.dyonovan.modernalchemy.tileentity.machines.TileCompressor;
import com.dyonovan.modernalchemy.tileentity.machines.TilePatternRecorder;
import com.dyonovan.modernalchemy.tileentity.machines.TileSolidifier;
import com.dyonovan.modernalchemy.tileentity.replicator.TileReplicatorCPU;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    public static final int BLAST_FURNACE_GUI_ID = 0;
    public static final int TESLA_COIL_GUI_ID = 1;
    public static final int COMPRESSOR_GUI_ID = 2;
    public static final int PATTERN_RECORDER_GUI_ID = 3;
    public static final int SOLIDIFIER_GUI_ID = 4;
    public static final int REPLICATOR_CPU_GUI_ID = 5;
    public static final int TESLA_COIL_LINKS_GUI_ID = 6;
    public static final int MANUAL_GUI_ID = 7;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case BLAST_FURNACE_GUI_ID :
                return new ContainerArcFurnace(player.inventory, (TileArcFurnaceCore) world.getTileEntity(x, y, z));
            case TESLA_COIL_GUI_ID :
                return new ContainerTeslaCoil((TileTeslaCoil) world.getTileEntity(x, y, z));
            case COMPRESSOR_GUI_ID :
                return new ContainerCompressor((TileCompressor) world.getTileEntity(x, y, z));
            case PATTERN_RECORDER_GUI_ID :
                return new ContainerPatternRecorder(player.inventory, (TilePatternRecorder) world.getTileEntity(x, y, z));
            case SOLIDIFIER_GUI_ID :
                return new ContainerSolidifier(player.inventory, (TileSolidifier) world.getTileEntity(x, y, z));
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
            case BLAST_FURNACE_GUI_ID :
                return new GuiArcFurnace(player.inventory, (TileArcFurnaceCore) world.getTileEntity(x, y, z));
            case TESLA_COIL_GUI_ID :
                return new GuiTeslaCoil((TileTeslaCoil) world.getTileEntity(x, y, z));
            case COMPRESSOR_GUI_ID :
                return new GuiCompressor((TileCompressor) world.getTileEntity(x, y, z));
            case PATTERN_RECORDER_GUI_ID :
                return new GuiPatternRecorder(player.inventory, (TilePatternRecorder) world.getTileEntity(x, y, z));
            case SOLIDIFIER_GUI_ID :
                return new GuiSolidifier(player.inventory, (TileSolidifier) world.getTileEntity(x, y, z));
            case REPLICATOR_CPU_GUI_ID:
                return new GuiReplicatorCPU(player.inventory, (TileReplicatorCPU) world.getTileEntity(x, y, z));
            case TESLA_COIL_LINKS_GUI_ID :
                return new GuiTeslaCoilLinks((TileTeslaCoil) world.getTileEntity(x, y, z));
            case MANUAL_GUI_ID :
                return ManualPages.instance.getPage(player.getCurrentEquippedItem().getTagCompound().getString("LastPage"));
        }
        return null;
    }
}
