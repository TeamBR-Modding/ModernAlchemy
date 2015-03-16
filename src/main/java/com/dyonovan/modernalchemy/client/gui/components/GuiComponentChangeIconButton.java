package com.dyonovan.modernalchemy.client.gui.components;

import com.dyonovan.modernalchemy.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import openmods.gui.component.GuiComponentButton;

public class GuiComponentChangeIconButton extends GuiComponentButton {

    public IIcon icon;

    public GuiComponentChangeIconButton(int x, int y, int color, IIcon icon) {
        super(x, y, icon.getIconWidth() + 4, icon.getIconHeight() + 4, color);
        this.icon = icon;
    }

    @Override
    public void renderContents(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY, boolean pressed) {
        RenderUtils.bindTextureSheet();
        int offset = (buttonEnabled && pressed)? 3 : 2;
        drawTexturedModelRectFromIcon(offsetX + x + offset, offsetY + y + offset, icon, icon.getIconWidth(), icon.getIconHeight());
    }

    @Override
    public void renderOverlay(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {}
}
