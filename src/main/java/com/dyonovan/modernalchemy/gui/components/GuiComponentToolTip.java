package com.dyonovan.modernalchemy.gui.components;

import cofh.api.energy.EnergyStorage;
import net.minecraft.client.Minecraft;
import openmods.api.IValueReceiver;
import openmods.gui.component.BaseComponent;

import java.util.ArrayList;
import java.util.List;

public class GuiComponentToolTip extends BaseComponent {
    private List<String> toolTip;
    int xSize;
    int ySize;
    public GuiComponentToolTip(int x, int y, int width, int height) {
        super(x, y);
        toolTip = new ArrayList<>();
        xSize = width;
        ySize = height;
    }

    public void addStringToTip(String str) {
        toolTip.add(str);
    }

    @Override
    public int getWidth() {
        return xSize;
    }

    @Override
    public int getHeight() {
        return ySize;
    }

    @Override
    public void render(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {}

    @Override
    public void renderOverlay(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
        if (toolTip != null && !toolTip.isEmpty() && isMouseOver(mouseX, mouseY)) {
            drawHoveringText(toolTip, offsetX + mouseX, offsetY + mouseY, minecraft.fontRenderer);
        }
    }

    public void setToolTip(List<String> value) {
        toolTip.clear();
        toolTip.addAll(value);
    }
}
