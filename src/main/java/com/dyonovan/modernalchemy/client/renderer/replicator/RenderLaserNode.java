package com.dyonovan.modernalchemy.client.renderer.replicator;

import com.dyonovan.modernalchemy.common.entities.EntityLaserNode;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.client.model.replicator.ModelLaserNode;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderLaserNode extends Render {

    private ModelLaserNode model;
    public static final ResourceLocation texture = new ResourceLocation(Constants.MODID + ":textures/models/laserNode.png");

    public RenderLaserNode() {
        model = new ModelLaserNode();
        shadowSize = 0.5F;
    }

    public void renderNode(EntityLaserNode entity, double x, double y, double z, float yaw, float partialTickTime) {
        GL11.glPushMatrix();

        GL11.glTranslatef((float) x, (float) y + 0.3F, (float) z);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);

        model.setRotation(entity.rotationYaw, entity.rotationPitch, entity.rotationPitch);

        this.bindTexture(texture);
        model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
        renderNode((EntityLaserNode) entity, x, y, z, yaw, partialTickTime);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
        //return new ResourceLocation(Constants.MODID + ":textures/models/laserNode.png");
    }
}
