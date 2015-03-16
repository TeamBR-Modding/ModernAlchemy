package com.dyonovan.modernalchemy.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import openmods.gui.component.BaseComponent;
import openmods.gui.component.BaseComposite;
import openmods.gui.component.GuiComponentTab;
import openmods.gui.misc.BoxRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiComponentReverseTab extends GuiComponentTab {

    private static final int FOLDED_WIDTH = 24;
    private static final int FOLDED_HEIGHT = 24;
    protected static RenderItem itemRenderer = new RenderItem();
    protected final int expandedWidth;
    protected final int expandedHeight;
    private boolean active = false;
    private ItemStack iconStack;
    private double dWidth = FOLDED_WIDTH;
    private double dHeight = FOLDED_HEIGHT;
    private int color;

    private static final BoxRenderer BOX_RENDERER = new BoxRenderer(0, 5) {
        @Override
        protected void renderTopRightCorner(Gui gui, int height) {}

        @Override
        protected void renderBottomRightCorner(Gui gui, int width, int height) {}

        @Override
        protected void renderRightEdge(Gui gui, int width, int height) {}
    };

    public GuiComponentReverseTab(int color, ItemStack iconStack, int expandedWidth, int expandedHeight, int guiSize) {
        super(color, iconStack, FOLDED_WIDTH, FOLDED_HEIGHT);
        this.expandedWidth = expandedWidth;
        this.expandedHeight = expandedHeight;
        this.iconStack = iconStack;
        this.color = color;
        setX(0);
    }

    @Override
    protected boolean areChildrenActive() {
        return active && width == expandedWidth && height == expandedHeight;
    }

    @Override
    public BaseComposite addComponent(BaseComponent component) {
        component.setX(component.getX() - 100);
        components.add(component);
        return this;
    }

    @Override
    public void renderComponentBackground(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
        double targetWidth = active? expandedWidth : FOLDED_WIDTH;
        double targetHeight = active? expandedHeight : FOLDED_HEIGHT;
        if (width != targetWidth) dWidth += (targetWidth - dWidth) / 4;
        if (height != targetHeight) dHeight += (targetHeight - dHeight) / 4;

        width = (int)Math.round(dWidth);
        height = (int)Math.round(dHeight);

        bindComponentsSheet();
        BOX_RENDERER.render(this, offsetX + x - width + 5, offsetY + y, width, height, color);

        GL11.glColor4f(1, 1, 1, 1);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        itemRenderer.renderItemAndEffectIntoGUI(minecraft.fontRenderer, minecraft.getTextureManager(), iconStack,
                offsetX + x + 8 - width, offsetY + y + 3);
        GL11.glColor3f(1, 1, 1);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    public boolean isOrigin(int x, int y) {
        return x < FOLDED_WIDTH && y < FOLDED_WIDTH;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x - getWidth() && mouseX <= x && mouseY >= y && mouseY < y + getHeight();
    }
}
