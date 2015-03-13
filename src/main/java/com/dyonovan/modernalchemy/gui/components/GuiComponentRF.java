package com.dyonovan.modernalchemy.gui.components;

import cofh.api.energy.EnergyStorage;
import com.dyonovan.modernalchemy.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import openmods.api.IValueReceiver;
import openmods.gui.component.GuiComponentResizable;
import openmods.gui.misc.BoxRenderer;
import openmods.utils.render.FakeIcon;

public class GuiComponentRF extends GuiComponentResizable {

    private static final BoxRenderer BOX_RENDERER = new BoxRenderer(0, 0);
    private static final int BORDER_COLOR = 0xc6c6c6;

    private EnergyStorage energy;

    public GuiComponentRF(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void render(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
        bindComponentsSheet();
        BOX_RENDERER.render(this, x + offsetX, y + offsetY, width, height, BORDER_COLOR);

        if(energy == null || energy.getEnergyStored() <= 0) return;

        RenderUtils.bindTextureSheet();

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_F(1, 1, 1);

        double percentFull = Math.max(0, Math.min(1, (double)energy.getEnergyStored() / (double)energy.getMaxEnergyStored()));
        double energyHeight = (height - 3) * percentFull;
        final int posX = offsetX + x;
        final int posY = offsetY + y;

        IIcon icon = FakeIcon.createSheetIcon(224, 0, 16, 52);
        final float minU = icon.getMinU();
        final float maxU = icon.getMaxU();

        final float minV = icon.getMinV();
        final float maxV = icon.getMaxV();

        tessellator.addVertexWithUV(posX + 3, posY + height - 3, this.zLevel, minU, maxV);
        tessellator.addVertexWithUV(posX + width - 3, posY + height - 3, this.zLevel, maxU, maxV);
        tessellator.addVertexWithUV(posX + width - 3, posY + (height - energyHeight), this.zLevel, maxU, minV);
        tessellator.addVertexWithUV(posX + 3, posY + (height - energyHeight), this.zLevel, minU, minV);
        tessellator.draw();
    }

    @Override
    public void renderOverlay(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {

    }

    public IValueReceiver<EnergyStorage> rfBankReciever() {
        return new IValueReceiver<EnergyStorage>() {
            @Override
            public void setValue(EnergyStorage value) {
                energy = value;
            }
        };
    }
}