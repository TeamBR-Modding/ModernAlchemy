package com.dyonovan.modernalchemy.manual.component;

import com.dyonovan.modernalchemy.crafting.CraftingRecipeHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ComponentCraftingRecipe extends ComponentBase {
    protected static ItemRenderer itemRender = new ItemRenderer(Minecraft.getMinecraft());
    protected ItemStack stack;

    public ComponentCraftingRecipe(ItemStack item) {
        stack = item;
    }

    @Override
    public int getSpace() {
        return 60;
    }

    @Override
    public void drawComponent(int x, int y, int mouseX, int mouseY) {

        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Constants.MODID + ":textures/gui/manual/arrow.png"));
        int drawX = x + 68;
        GL11.glPushMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableGUIStandardItemLighting();
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(drawX, y + 20, 0, 0.0, 0.0);
        tess.addVertexWithUV(drawX, y + 20 + 16, 0, 0.0, 1.0);
        tess.addVertexWithUV(drawX + 22, y + 20 + 16, 0, 1.0, 1.0);
        tess.addVertexWithUV(drawX + 22, y + 20, 0, 1.0, 0.0);
        tess.draw();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();

        RenderHelper.enableGUIStandardItemLighting();

        ItemStack[] recipe = CraftingRecipeHandler.getRecipe(stack);
        int xStart = 5;
        int space = 17;
        int rX = xStart;
        int rY = 13;
        for(ItemStack stack : recipe) {
            rX += space;
            if (rX > xStart + (space * 3)) {
                rX = xStart + space;
                rY += space;
            }
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_CULL_FACE);
            if(stack != null && stack.getItem() instanceof ItemBlock) {
                GL11.glTranslated(x + rX, y + rY, 20);
                GL11.glRotated(150, 1.0, 0.0, 0.0);
                GL11.glRotated(-45, 0.0, 1.0, 0.0);
            }
            else {
                GL11.glTranslated(x + rX - 5, y + rY + 5, 20);
                GL11.glRotated(-60, 0.0, 1.0, 0.0);
                GL11.glRotated(21, 0.0, 0.0, 1.0);
                GL11.glRotated(15, 1.0, 0.0, 0.0);
            }
            GL11.glScaled(-9, -9, -9);
            if (stack != null) {
                itemRender.renderItem(Minecraft.getMinecraft().thePlayer, stack, stack.getItemDamage(), IItemRenderer.ItemRenderType.INVENTORY);
            }
            GL11.glPopMatrix();
        }
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glTranslated(x + 95, y + 15, 20);
        GL11.glRotated(150, 1.0, 0.0, 0.0);
        GL11.glRotated(-90, 0.0, 1.0, 0.0);
        GL11.glScaled(15, 15, 15);
        itemRender.renderItem(Minecraft.getMinecraft().thePlayer, stack, stack.getItemDamage(), IItemRenderer.ItemRenderType.INVENTORY);

        GL11.glPopMatrix();
        super.drawComponent(x, y, mouseX, mouseY);
    }
}
