package com.dyonovan.modernalchemy.manual.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ComponentImage extends ComponentBase {
    protected ResourceLocation image;

    public ComponentImage(int width, int height, ResourceLocation resource) {
        image = resource;
    }


    @Override
    public void drawComponent(int x, int y, int mouseX, int mouseY) {
        Minecraft.getMinecraft().renderEngine.bindTexture(image);
        int drawX = x + 15;
        GL11.glPushMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableGUIStandardItemLighting();
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(drawX, y, 0, 0.0, 0.0);
        tess.addVertexWithUV(drawX, y + height, 0, 0.0, 1.0);
        tess.addVertexWithUV(drawX + width, y + height, 0, 1.0, 1.0);
        tess.addVertexWithUV(drawX + width, y, 0, 1.0, 0.0);
        tess.draw();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        super.drawComponent(x, y, mouseX, mouseY);
    }
}
