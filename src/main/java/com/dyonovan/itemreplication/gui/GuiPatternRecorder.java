package com.dyonovan.itemreplication.gui;

import com.dyonovan.itemreplication.container.ContainerPatternRecorder;
import com.dyonovan.itemreplication.tileentity.TilePatternRecorder;
import net.minecraft.client.gui.inventory.GuiContainer;

/**
 * Created by Tim on 2/8/2015.
 */
public class GuiPatternRecorder extends GuiContainer {

    public GuiPatternRecorder(TilePatternRecorder tileEntity){
        super(new ContainerPatternRecorder(tileEntity));

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {

    }
}
