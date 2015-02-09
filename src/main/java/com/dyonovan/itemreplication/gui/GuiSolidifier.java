package com.dyonovan.itemreplication.gui;

import com.dyonovan.itemreplication.container.ContainerSolidifier;
import com.dyonovan.itemreplication.tileentity.TileSolidifier;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiSolidifier extends GuiContainer  {

    public GuiSolidifier(TileSolidifier tile) {
        super(new ContainerSolidifier(tile));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {

    }
}
