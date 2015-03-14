package com.dyonovan.modernalchemy.client.manual.component;

import com.dyonovan.modernalchemy.client.manual.pages.GuiManual;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ComponentBase extends GuiScreen implements IComponent {
    protected ALIGNMENT alignment = ALIGNMENT.CENTER;
    protected List<String> toolTip = new ArrayList<String>();
    private int delay = 0;
    public int xPos = 0;
    public int yPos = 0;
    public int width = 0;
    public int height = 0;

    public void setAlignment(ALIGNMENT al) {
        alignment = al;
    }

    public void setPositionAndSize(int x, int y, int w, int h) {
        xPos = x;
        yPos = y;
        width = w;
        height = h;
    }

    public void addToTip(String tip) {
        toolTip.add(tip);
    }

    @Override
    public void drawComponent(int x, int y, int mouseX, int mouseY) {
        if(mouseX > x + xPos && mouseX < x + xPos + width && mouseY > y + yPos && mouseY <= y + yPos + height && !toolTip.isEmpty()) {
            if(++delay > 20)
                drawHoveringText(toolTip, mouseX, mouseY, Minecraft.getMinecraft().fontRenderer);
        }
        else
            delay = 0;

        if(mouseX > x + xPos && mouseX < x + xPos + width && mouseY > y + yPos && mouseY <= y + yPos + height) {
            if(Mouse.isButtonDown(0) && GuiManual.inputDelay < 0)
                onMouseLeftClick();
        }
    }

    public void onMouseLeftClick() {
        GuiManual.inputDelay = 50;
        GuiManual.playClickSound();
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

    protected void drawGradientRect(int x1, int y1, int x2, int y2, int color, int gradient)
    {
        float f = (float)(color >> 24 & 255) / 255.0F;
        float f1 = (float)(color >> 16 & 255) / 255.0F;
        float f2 = (float)(color >> 8 & 255) / 255.0F;
        float f3 = (float)(color & 255) / 255.0F;
        float f4 = (float)(gradient >> 24 & 255) / 255.0F;
        float f5 = (float)(gradient >> 16 & 255) / 255.0F;
        float f6 = (float)(gradient >> 8 & 255) / 255.0F;
        float f7 = (float)(gradient & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        float zLevel = 0;
        tessellator.addVertex((double)x2, (double)y1, (double) zLevel);
        tessellator.addVertex((double)x1, (double)y1, (double) zLevel);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex((double)x1, (double)y2, (double) zLevel);
        tessellator.addVertex((double)x2, (double)y2, (double) zLevel);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    protected void drawHoveringText(java.util.List toolTip, int x, int y, FontRenderer font)
    {
        if (!toolTip.isEmpty())
        {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator iterator = toolTip.iterator();

            while (iterator.hasNext())
            {
                String s = (String)iterator.next();
                int l = font.getStringWidth(s);

                if (l > k)
                {
                    k = l;
                }
            }

            int j2 = x + 5;
            int k2 = y - 12;
            int i1 = 8;

            if (toolTip.size() > 1)
            {
                i1 += 2 + (toolTip.size() - 1) * 10;
            }

            this.zLevel = 300.0F;
            int j1 = -267386864;
            this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

            for (int i2 = 0; i2 < toolTip.size(); ++i2)
            {
                String s1 = (String)toolTip.get(i2);
                font.drawStringWithShadow(s1, j2, k2, -1);

                if (i2 == 0)
                {
                    k2 += 2;
                }

                k2 += 10;
            }

            this.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }
}
