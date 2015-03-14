package com.dyonovan.modernalchemy.client.manual.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ComponentImage extends ComponentBase {
    protected ResourceLocation image;

    public ComponentImage(ResourceLocation resource) {
        image = resource;
    }

    @Override
    public void drawComponent(int x, int y, int mouseX, int mouseY) {
        Minecraft.getMinecraft().renderEngine.bindTexture(image);
        int drawX = x + xPos;
        int drawY = y + yPos;
        GL11.glPushMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableGUIStandardItemLighting();
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(drawX, drawY, 0, 0.0, 0.0);
        tess.addVertexWithUV(drawX, drawY + height, 0, 0.0, 1.0);
        tess.addVertexWithUV(drawX + width, drawY + height, 0, 1.0, 1.0);
        tess.addVertexWithUV(drawX + width, drawY, 0, 1.0, 0.0);
        tess.draw();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        super.drawComponent(x, y, mouseX, mouseY);
    }
}
