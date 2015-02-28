package com.dyonovan.modernalchemy.manual.component;

import com.dyonovan.modernalchemy.handlers.BlockHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ComponentItemRender extends ComponentBase {
    protected static ItemRenderer itemRender = new ItemRenderer(Minecraft.getMinecraft());
    protected ItemStack stack;
    protected double scale;

    public ComponentItemRender(double size, ItemStack itemStack) {
        stack = itemStack;
        scale = size;
    }

    @Override
    public void drawComponent(int x, int y, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        RenderHelper.enableGUIStandardItemLighting();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if(!stack.getDisplayName().contains("Tesla Coil"))
            GL11.glTranslated(x + (115 / 2) + 12, y + scale, 0);
        else
            GL11.glTranslated(x + (115 / 2) - 10, y + 10, 0);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glRotated(150, 1.0, 0.0, 0.0);
        GL11.glRotated(-135, 0.0, 1.0, 0.0);
        GL11.glScaled(scale, scale, scale);

        itemRender.renderItem(Minecraft.getMinecraft().thePlayer, stack, stack.getItemDamage(), IItemRenderer.ItemRenderType.INVENTORY);

        GL11.glPopMatrix();
        super.drawComponent(x, y, mouseX, mouseY);
    }
}
