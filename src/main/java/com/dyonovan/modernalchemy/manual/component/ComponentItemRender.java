package com.dyonovan.modernalchemy.manual.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ComponentItemRender extends ComponentBase {
    protected static ItemRenderer itemRender = new ItemRenderer(Minecraft.getMinecraft());
    protected ItemStack stack;
    protected double scale;
    protected double roll = 0;
    protected double yaw = 0;
    protected double offset = 0.1;
    protected boolean rotate = true;

    public ComponentItemRender(double size, boolean doRotate, ItemStack itemStack) {
        stack = itemStack;
        scale = size;
        rotate = doRotate;
    }

    @Override
    public int getSpace() {
        return (int) (scale + 20);
    }

    @Override
    public void drawComponent(int x, int y, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslated(x + (115 / 2) - 5, y + 3, 0);
        GL11.glDisable(GL11.GL_CULL_FACE);

        scale = 30F;

        GL11.glRotated(150, 1.0, 0.0, 0.0);
        GL11.glRotated(-90, 0.0, 1.0, 0.0);
        GL11.glScaled(scale, scale, scale);

        itemRender.renderItem(Minecraft.getMinecraft().thePlayer, stack, stack.getItemDamage(), IItemRenderer.ItemRenderType.INVENTORY);

        GL11.glPopMatrix();
        super.drawComponent(x, y, mouseX, mouseY);
    }
}
