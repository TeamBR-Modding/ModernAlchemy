package com.dyonovan.modernalchemy.manual.component;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ComponentBase implements IComponent {
    protected ALIGNMENT alignment = ALIGNMENT.CENTER;

    public void setAlignment(ALIGNMENT al) {
        alignment = al;
    }

    @Override
    public int getSpace() {
        return 0;
    }

    @Override
    public void drawComponent(int x, int y, int mouseX, int mouseY) {

    }

    public void drawRectangle(int x1, int y1, int x2, int y2, Color color) {
        GL11.glPushMatrix();
        Tessellator tess = Tessellator.instance;
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        tess.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        tess.startDrawingQuads();
        tess.addVertex(x1, y1, 0);
        tess.addVertex(x2, y1, 0);
        tess.addVertex(x2, y2, 0);
        tess.addVertex(x1, y2, 0);
        tess.draw();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
