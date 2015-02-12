package com.dyonovan.itemreplication.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelReplicatorStand extends ModelBase {
    //fields
    ModelRenderer top;
    ModelRenderer shaft;
    ModelRenderer base;

    public ModelReplicatorStand() {
        textureWidth = 64;
        textureHeight = 64;

        top = new ModelRenderer(this, 0, 16);
        top.addBox(-4F, 0F, -4F, 8, 1, 8);
        top.setRotationPoint(0F, 11F, 0F);
        top.setTextureSize(64, 64);
        top.mirror = true;
        setRotation(top, 0F, 0F, 0F);
        shaft = new ModelRenderer(this, 0, 25);
        shaft.addBox(-2F, 0F, -2F, 4, 10, 4);
        shaft.setRotationPoint(0F, 12F, 0F);
        shaft.setTextureSize(64, 64);
        shaft.mirror = true;
        setRotation(shaft, 0F, 0F, 0F);
        base = new ModelRenderer(this, 0, 0);
        base.addBox(-7F, 0F, -7F, 14, 2, 14);
        base.setRotationPoint(0F, 22F, 0F);
        base.setTextureSize(64, 64);
        base.mirror = true;
        setRotation(base, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        top.render(f5);
        shaft.render(f5);
        base.render(f5);
    }

    public void renderModel(float f, int x, int y, int z, boolean inHand) {
        top.render(f);
        shaft.render(f);
        base.render(f);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }

}
