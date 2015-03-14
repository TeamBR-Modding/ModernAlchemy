package com.dyonovan.modernalchemy.client.gui.widgets;


import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.common.tileentity.BaseMachine;
import com.dyonovan.modernalchemy.client.gui.widget.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class WidgetPulse extends Widget {

    private BaseMachine tileEntity;
    private float alpha;
    private float offSet = 0.005F;
    private int rot = 0;

    /**
     * @param gui Parent Gui
     * @param x   How far from guiLeft
     * @param y   How far down from guiTop
     */
    public WidgetPulse(Gui gui, BaseMachine tile, int x, int y) {
        super(gui, x, y);
        tileEntity = tile;
    }

    @Override
    public void render(int x, int y) {
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Constants.MODID + ":textures/gui/pulse.png"));
        if(tileEntity.isActive()) {
            alpha += offSet;
            rot++;
            if(alpha >= 1.0F) {
                offSet = -0.005F;
            } else if(alpha <= 0.2F){
                offSet = 0.005F;
            }

            GL11.glPushMatrix();
            GL11.glDepthMask(false);
            GL11.glEnable(3042);
            GL11.glDisable(2884);
            GL11.glTranslated(x + xPos + 13, y + yPos - 11, 0);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
            GL11.glRotatef(rot, 0.0F, 0.0F, 1.0F);
            Tessellator tess = Tessellator.instance;

            tess.startDrawingQuads();
            tess.addVertexWithUV(-13, 11, 0, 0.0F, 1.0F);
            tess.addVertexWithUV(13, 11, 0, 1.0F, 1.0F);
            tess.addVertexWithUV(13, -11, 0, 1.0F, 0.0F);
            tess.addVertexWithUV(-13, -11, 0, 0.0F, 0.0F);
            tess.draw();

            GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha - 0.2F);

            GL11.glRotatef(-rot * 2, 0.0F, 0.0F, 1.0F);
            tess.startDrawingQuads();
            tess.addVertexWithUV(-13 - getOffset(), 11 + getOffset(), 0, 0.0F, 1.0F);
            tess.addVertexWithUV(13 + getOffset(), 11 + getOffset(), 0, 1.0F, 1.0F);
            tess.addVertexWithUV(13 + getOffset(), -11 - getOffset(), 0, 1.0F, 0.0F);
            tess.addVertexWithUV(-13 - getOffset(), -11 - getOffset(), 0, 0.0F, 0.0F);
            tess.draw();

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(2884);
            GL11.glDisable(3042);
            GL11.glDepthMask(true);
            GL11.glPopMatrix();
        }
    }

    private float getOffset() {
        return alpha * 5;
    }
}
