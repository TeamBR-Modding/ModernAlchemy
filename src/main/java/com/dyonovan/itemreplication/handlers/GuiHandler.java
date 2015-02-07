package com.dyonovan.itemreplication.handlers;

import com.dyonovan.itemreplication.container.ContainerArcFurnace;
import com.dyonovan.itemreplication.container.ContainerTeslaCoil;
import com.dyonovan.itemreplication.gui.GuiArcFurnace;
import com.dyonovan.itemreplication.gui.GuiTeslaCoil;
import com.dyonovan.itemreplication.tileentity.TileArcFurnaceCore;
import com.dyonovan.itemreplication.tileentity.TileTeslaCoil;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    public static final int BLAST_FURNACE_GUI_ID = 0;
    public static final int TESLA_COIL_GUI_ID = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
        case BLAST_FURNACE_GUI_ID :
            return new ContainerArcFurnace(player.inventory, (TileArcFurnaceCore) world.getTileEntity(x, y, z));
        case TESLA_COIL_GUI_ID :
            return new ContainerTeslaCoil((TileTeslaCoil) world.getTileEntity(x, y, z));
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
        }
        return null;
    }
}
