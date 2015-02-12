package com.dyonovan.itemreplication.gui;

import com.dyonovan.itemreplication.container.ContainerFrameEnergy;
import com.dyonovan.itemreplication.tileentity.TileFrameEnergy;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiFrameEnergy extends BaseGui {

    public GuiFrameEnergy(TileFrameEnergy tileEntity) {
        super(new ContainerFrameEnergy(tileEntity));

    }
}
