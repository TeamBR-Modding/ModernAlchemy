package com.dyonovan.itemreplication.handlers;

import com.dyonovan.itemreplication.container.*;
import com.dyonovan.itemreplication.gui.*;
import com.dyonovan.itemreplication.tileentity.*;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    public static final int BLAST_FURNACE_GUI_ID = 0;
    public static final int TESLA_COIL_GUI_ID = 1;
    public static final int COMPRESSOR_GUI_ID = 2;
    public static final int PATTERN_RECORDER_GUI_ID = 3;
    public static final int SOLIDIFIER_GUI_ID = 4;
    public static final int FRAME_ENERGY_GUI_ID = 5;

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
        case FRAME_ENERGY_GUI_ID :
                return new ContainerFrameEnergy((TileFrameEnergy) world.getTileEntity(x, y, z));
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
        case FRAME_ENERGY_GUI_ID :
            return new GuiFrameEnergy((TileFrameEnergy) world.getTileEntity(x, y, z));
        }
        return null;
    }
}
