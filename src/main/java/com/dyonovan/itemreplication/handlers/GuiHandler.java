package com.dyonovan.itemreplication.handlers;

import com.dyonovan.itemreplication.container.ContainerArcFurnace;
import com.dyonovan.itemreplication.container.ContainerCompressor;
import com.dyonovan.itemreplication.container.ContainerPatternRecorder;
import com.dyonovan.itemreplication.container.ContainerSolidifier;
import com.dyonovan.itemreplication.container.ContainerTeslaCoil;
import com.dyonovan.itemreplication.gui.GuiArcFurnace;
import com.dyonovan.itemreplication.gui.GuiCompressor;
import com.dyonovan.itemreplication.gui.GuiPatternRecorder;
import com.dyonovan.itemreplication.gui.GuiSolidifier;
import com.dyonovan.itemreplication.gui.GuiTeslaCoil;
import com.dyonovan.itemreplication.tileentity.TileArcFurnaceCore;
import com.dyonovan.itemreplication.tileentity.TileCompressor;
import com.dyonovan.itemreplication.tileentity.TilePatternRecorder;
import com.dyonovan.itemreplication.tileentity.TileSolidifier;
import com.dyonovan.itemreplication.tileentity.TileTeslaCoil;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    public static final int BLAST_FURNACE_GUI_ID = 0;
    public static final int TESLA_COIL_GUI_ID = 1;
    public static final int COMPRESSOR_GUI_ID = 2;
    public static final int PATTERN_RECORDER_GUI_ID = 3;
    public static final int SOLIDIFIER_GUI_ID = 4;

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
            return new ContainerPatternRecorder((TilePatternRecorder) world.getTileEntity(x, y, z));
        case SOLIDIFIER_GUI_ID :
            return new ContainerSolidifier((TileSolidifier) world.getTileEntity(x, y, z));
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
            return new GuiPatternRecorder((TilePatternRecorder) world.getTileEntity(x, y, z));
        case SOLIDIFIER_GUI_ID :
            return new GuiSolidifier((TileSolidifier) world.getTileEntity(x, y, z));
        }
        return null;
    }
}
