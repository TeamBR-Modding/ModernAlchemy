package com.dyonovan.modernalchemy.manual.component;

import com.dyonovan.modernalchemy.helpers.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ComponentTitle implements IComponent {
    private String title;
    private ALIGNMENT alignment = ALIGNMENT.CENTER;

    public ComponentTitle(String label) {
        title = label;
    }

    public void setTitle(String string) {
        title = string;
        title.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setAlignment(ALIGNMENT al) {
        alignment = al;
    }

    @Override
    public void drawComponent(int x, int y, int mouseX, int mouseY) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int drawX = x;
        switch(alignment) {
            case RIGHT :
            drawX += 100 - fontRenderer.getStringWidth(title);
                break;
            case CENTER :
                drawX += 67 - (fontRenderer.getStringWidth(title) / 2);
                break;
            case LEFT :
            default :
                drawX += 15;
                break;
        }
        int drawY = y + yPos + 16;
        fontRenderer.drawSplitString(title, drawX, drawY, 120, 4210752);
        drawHorizontalLine(x + 15, drawY + 10, x + 120, drawY + 11, new Color(255, 255, 255));
    }

    public void drawHorizontalLine(int x1, int y1, int x2, int y2, Color color) {
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
