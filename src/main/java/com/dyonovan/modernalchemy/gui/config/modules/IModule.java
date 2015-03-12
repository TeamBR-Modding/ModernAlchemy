package com.dyonovan.modernalchemy.gui.config.modules;

import net.minecraft.client.gui.FontRenderer;

public interface IModule {
    public void drawGuiContainerBackgroundLayer(int x, int y, float f, int i, int j);
    public void drawGuiContainerForegroundLayer(FontRenderer fontRenderer, int par1, int par2);
}
