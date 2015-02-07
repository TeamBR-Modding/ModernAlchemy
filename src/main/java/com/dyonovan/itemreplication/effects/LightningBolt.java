package com.dyonovan.itemreplication.effects;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class LightningBolt extends EntityFX {

    public LightningBolt(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    public void renderParticle(Tessellator tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        super.renderParticle(tessellator, par2, par3, par4, par5, par6, par7);
        float x = (float)(this.posX - interpPosX);
        float y = (float)(this.posY - interpPosY);
        float z = (float)(this.posZ - interpPosZ);

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPointSize(10.0F);
        GL11.glBegin(GL11.GL_POINTS);

        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y + 10, z);
        GL11.glEnd();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    public void onUpdate()
    {

    }

}
